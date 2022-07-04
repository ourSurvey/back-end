package com.oursurvey.controller;

import com.oursurvey.dto.AuthDto;
import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.TokenDto;
import com.oursurvey.dto.repo.user.UserDto;
import com.oursurvey.exception.*;
import com.oursurvey.service.user.UserService;
import com.oursurvey.util.JwtUtil;
import com.oursurvey.util.MailUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;
    private final MailUtil mailUtil;

    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redis;

    @Value("${spring.redis.prefix.key}")
    private String REDIS_PREFIX_KEY;
    @Value("${spring.mail.prefix.key}")
    private String MAIL_PREFIX_KEY;

    // 로그인
    @PostMapping("/login")
    public MyResponse login(@Validated @RequestBody AuthDto.Login dto, BindingResult br) throws Exception {
        MyResponse res = new MyResponse();
        if (br.hasFieldErrors()) {
            throw new InvalidFormException("invalid form");
        }

        Optional<UserDto.Basic> opt = service.findByEmail(dto.getEmail());
        if (opt.isEmpty()) {
            throw new AuthFailException("invalid id or pass");
        }

        UserDto.Basic user = opt.get();
        if (!encoder.matches(dto.getPwd(), user.getPwd())) {
            throw new AuthFailException("invalid id or pass");
        }

        TokenDto token = jwtUtil.createToken(user.getId());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "Bearer");
        dataMap.put("access", token.getAccessToken());
        dataMap.put("refresh", token.getRefreshToken());
        dataMap.put("refreshExpire", token.getRefreshTokenExpire());

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + user.getId(), token.getRefreshToken(), JwtUtil.REFRESH_TOKEN_PERIOD, TimeUnit.SECONDS);

        return res;
    }

    @PostMapping("/join")
    public MyResponse join(@Validated @RequestBody AuthDto.Join dto, BindingResult br) {
        MyResponse res = new MyResponse();
        if (br.hasFieldErrors()) {
            throw new InvalidFormException("invalid form");
        }

        Long joinUser = service.create(UserDto.Create.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(encoder.encode(dto.getPwd()))
                .build());

        HashMap<String, Long> dataMap = new HashMap<>();
        dataMap.put("user", joinUser);
        return res.setData(dataMap);
    }

    // 인증번호 발송
    @PostMapping("/take")
    public MyResponse take(@RequestBody String email) throws Exception {
        MyResponse res = new MyResponse();

        String code = mailUtil.generateAuthCode();
        mailUtil.sendMail(email, "인증코드", code);

        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(MAIL_PREFIX_KEY + email, code, 180, TimeUnit.SECONDS);
        return res;
    }

    // 인증번호 확인
    @PostMapping("/certified")
    public MyResponse certified(@RequestBody AuthDto.Certified dto) throws Exception {
        MyResponse res = new MyResponse();
        ValueOperations<String, Object> vop = redis.opsForValue();
        String code = String.valueOf(vop.get(MAIL_PREFIX_KEY + dto.getEmail()));
        if (code == null) {
            throw new CertifiedException("invalid code");
        }

        if (!code.equals(dto.getCode())) {
            throw new CertifiedException("not matched code");
        }

        return res;
    }

    @GetMapping("/logout")
    public MyResponse logout(HttpServletRequest request) {
        MyResponse res = new MyResponse();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new InvalidTokenException();
        }

        Boolean isValid = jwtUtil.validateToken(header);
        if (!isValid) {
            throw new InvalidRefreshTokenException();
        }

        Claims claims = jwtUtil.parseToken(header);
        String tokenType = claims.get("tokenType", String.class);
        if (!tokenType.equals("refresh")) {
            throw new InvalidRefreshTokenException("not refresh token");
        }

        Long id = claims.get("id", Long.class);
        String redisKey = REDIS_PREFIX_KEY + id;
        String token = jwtUtil.extractToken(header);
        ValueOperations<String, Object> vop = redis.opsForValue();
        if (!vop.get(redisKey).equals(token)) {
            throw new InvalidRefreshTokenException();
        }

        request.getSession().invalidate();
        vop.set(REDIS_PREFIX_KEY + id, token, 1, TimeUnit.MILLISECONDS);
        return res;
    }

    // access token 리프레시
    @GetMapping("/refresh")
    public MyResponse refresh(HttpServletRequest request) throws Exception {
        MyResponse res = new MyResponse();

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Claims claims = jwtUtil.parseToken(header);

        Long id = claims.get("id", Long.class);
        TokenDto token = jwtUtil.createToken(id, true);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "Bearer");
        dataMap.put("access", token.getAccessToken());
        return res.setData(dataMap);
    }

}
