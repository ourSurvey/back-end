package com.oursurvey.controller;

import com.oursurvey.dto.AuthDto;
import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.TokenDto;
import com.oursurvey.dto.repo.ExperienceDto;
import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.dto.repo.PointDto;
import com.oursurvey.dto.repo.UserDto;
import com.oursurvey.entity.Experience;
import com.oursurvey.entity.Point;
import com.oursurvey.exception.*;
import com.oursurvey.service.TransactionService;
import com.oursurvey.service.loggedin.LoggedInService;
import com.oursurvey.service.point.PointService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;
    private final PointService pointService;
    private final LoggedInService logService;
    private final TransactionService tService;

    private final PasswordEncoder encoder;
    private final RedisTemplate<String, Object> redis;
    private final JwtUtil jwtUtil;
    private final MailUtil mailUtil;

    @Value("${spring.redis.prefix.key}")
    private String REDIS_PREFIX_KEY;
    @Value("${spring.mail.prefix.key}")
    private String MAIL_PREFIX_KEY;

    // NOTE. [point ++, experience ++]
    @PostMapping("/login")
    public MyResponse login(HttpServletRequest request, @Validated @RequestBody AuthDto.Login dto, BindingResult br) throws Exception {
        MyResponse res = new MyResponse();
        if (br.hasFieldErrors()) {
            throw new InvalidFormException("invalid form");
        }

        Optional<UserDto.Basic> opt = service.findByEmail(dto.getEmail());
        if (opt.isEmpty()) {
            throw new LoginIdException("invalid id");
        }

        UserDto.Basic user = opt.get();
        if (!encoder.matches(dto.getPwd(), user.getPwd())) {
            throw new LoginPwdException("invalid pwd");
        }

        TokenDto token = jwtUtil.createToken(user.getId());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "Bearer");
        dataMap.put("access", token.getAccessToken());
        dataMap.put("refresh", token.getRefreshToken());
        dataMap.put("refreshExpire", token.getRefreshTokenExpire());
        dataMap.put("sumPoint", pointService.findSumByUserId(user.getId()));
        dataMap.put("savedPoint", null);

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + user.getId(), token.getRefreshToken(), JwtUtil.REFRESH_TOKEN_PERIOD, TimeUnit.SECONDS);

        // log & point & experience
        Optional<LoggedInDto.Base> logOpt = logService.findByUserIdDate(user.getId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (logOpt.isEmpty()) {
            tService.saveLogAndPointAndExperience(
                    LoggedInDto.Create.builder().userId(user.getId()).remoteAddr(request.getRemoteAddr()).build(),
                    PointDto.Create.builder()
                    .userId(user.getId())
                    .value(Point.LOGIN_VALUE)
                    .reason(Point.LOGIN_REASON)
                    .tablePk(user.getId())
                    .tableName("user")
                    .build(),
                    ExperienceDto.Create.builder()
                    .userId(user.getId())
                    .value(Experience.LOGIN_VALUE)
                    .reason(Experience.LOGIN_REASON)
                    .tablePk(user.getId())
                    .tableName("user")
                    .build());

            dataMap.replace("savedPoint", Point.LOGIN_VALUE);
        }

        return res.setData(dataMap);
    }

    // NOTE. [point ++]
    @PostMapping("/join")
    public MyResponse join(@Validated @RequestBody AuthDto.Join dto, BindingResult br) {
        MyResponse res = new MyResponse();
        if (br.hasFieldErrors()) {
            throw new InvalidFormException("invalid form");
        }

        Optional<UserDto.Basic> existMail = service.findByEmail(dto.getEmail());
        if (existMail.isPresent()) {
            throw new DuplicateEmailException("duplicate email");
        }

        Long joinUser = service.create(UserDto.Create.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(encoder.encode(dto.getPwd()))
                .build());

        TokenDto token = jwtUtil.createToken(joinUser, true);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "Bearer");
        dataMap.put("access", token.getAccessToken());
        dataMap.put("savedPoint", Point.JOIN_VALUE);

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + joinUser, token.getAccessToken(), JwtUtil.REFRESH_TOKEN_PERIOD, TimeUnit.SECONDS);
        return res.setData(dataMap);
    }

    @PostMapping("/addition")
    public MyResponse addition(HttpServletRequest request, @RequestBody AuthDto.Addition dto) {
        MyResponse res = new MyResponse();

        Long id = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        service.updateAddition(id, UserDto.UpdateAddition.builder().gender(dto.getGender()).age(dto.getAge()).tel(dto.getTel()).build());
        return res;
    }

    // 인증번호 발송
    @PostMapping("/take")
    public MyResponse take(@RequestBody HashMap<String, String> map) throws Exception {
        MyResponse res = new MyResponse();
        String email = map.get("email");
        String code = mailUtil.generateAuthCode();
        mailUtil.sendMail(email, "인증코드", code);

        ValueOperations<String, Object> vop = redis.opsForValue();
        System.out.println("ddd = " + (String) vop.get("ddd"));
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

    // 인증번호 발송
    @PostMapping("/findpwd")
    public MyResponse findpwd(@RequestBody HashMap<String, String> map) throws Exception {
        MyResponse res = new MyResponse();

        String email = map.get("email");
        String code = mailUtil.generateAuthCode();
        mailUtil.sendMail(email, "인증코드", code);

        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(MAIL_PREFIX_KEY + email, code, 180, TimeUnit.SECONDS);

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("token", jwtUtil.createToken(email, 300));
        return res.setData(dataMap);
    }

    @PostMapping("/resetpwd")
    public MyResponse resetpwd(@RequestBody AuthDto.Resetpwd dto) throws Exception {
        MyResponse res = new MyResponse();

        if (!jwtUtil.validateToken(dto.getToken(), true)) {
            throw new AuthFailException("invalid token");
        }

        Claims claims = jwtUtil.parseTokenString(dto.getToken());
        String email = claims.get("string", String.class);

        service.updatePwd(email, encoder.encode(dto.getPwd()));
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
        if (header == null) {
            throw new InvalidTokenException();
        }

        Boolean validate = jwtUtil.validateToken(header);
        if (!validate) {
            throw new InvalidTokenException();
        }

        Claims claims = jwtUtil.parseToken(header);
        String subject = claims.getSubject();
        String tokenType = claims.get("tokenType", String.class);
        if (!subject.equals("refreshToken") || !tokenType.equals("refresh")) {
            throw new InvalidRefreshTokenException();
        }

        Long id = claims.get("id", Long.class);
        TokenDto token = jwtUtil.createToken(id, true);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenType", "Bearer");
        dataMap.put("access", token.getAccessToken());
        return res.setData(dataMap);
    }

    @GetMapping("/validate")
    public MyResponse validate(HttpServletRequest request) throws Exception {
        MyResponse res = new MyResponse();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return res.setCode(MyResponse.INVALID_TOKEN).setData(false);
        }

        Boolean validate = jwtUtil.validateToken(header);
        if (!validate) {
            return res.setCode(MyResponse.INVALID_TOKEN).setData(false);
        }

        Claims claims = jwtUtil.parseToken(header);
        String subject = claims.getSubject();
        String tokenType = claims.get("tokenType", String.class);
        if (!subject.equals("accessToken") || !tokenType.equals("access")) {
            return res.setCode(MyResponse.INVALID_ACCESSTOKEN).setData(false);
        }

        return res.setData(true);
    }

}
