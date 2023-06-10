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
import com.oursurvey.jwt.TokenProvider;
import com.oursurvey.jwt.TokenType;
import com.oursurvey.service.experience.ExperienceService;
import com.oursurvey.service.loggedin.LoggedInService;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.user.UserService;
import com.oursurvey.jwt.JwtUtil;
import com.oursurvey.util.MailUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.util.TokenPrinters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ExperienceService experienceService;

    private final PasswordEncoder encoder;
    private final RedisTemplate<String, Object> redis;
    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MailUtil mailUtil;

    @Value("${custom.redis.prefix.key}")
    private String REDIS_PREFIX_KEY;
    @Value("${custom.mail.prefix.key}")
    private String MAIL_PREFIX_KEY;


    @PostMapping("/login")
    public MyResponse login(HttpServletRequest request, @RequestBody AuthDto.Login dto) throws Exception {
        log.warn("login :: ");
        UserDto.Basic user = service.findByEmail(dto.getEmail()).orElseThrow(() -> {
            throw new LoginIdException("invalid id");
        });

        if (!encoder.matches(dto.getPwd(), user.getPwd())) {
            throw new LoginPwdException("invalid pwd");
        }

        Authentication authentication = createAuthentication(user.getId(), dto.getPwd());
        String accessToken = tokenProvider.createToken(authentication, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenProvider.createToken(authentication, TokenType.REFRESH_TOKEN);

        Integer sumPoint = pointService.findSumByUserId(user.getId());
        AuthDto.LoginReponse responseData = AuthDto.LoginReponse.builder()
                .tokenType("Bearer")
                .access(accessToken)
                .refresh(refreshToken)
                .refreshExpire(TokenType.REFRESH_TOKEN.getHours())
                .sumPoint(sumPoint)
                .savedPoint(0)
                .build();

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + user.getId(), refreshToken, TokenType.REFRESH_TOKEN.getHours(), TimeUnit.HOURS);

        // log & point & experience
        Optional<LoggedInDto.Base> logOpt = logService.findByUserIdDate(user.getId(), LocalDate.now());
        if (logOpt.isEmpty()) {
            logService.create(LoggedInDto.Create.builder()
                    .userId(user.getId())
                    .remoteAddr(request.getRemoteAddr())
                    .build());

            pointService.create(PointDto.Create.builder()
                    .userId(user.getId())
                    .value(Point.LOGIN_VALUE)
                    .reason(Point.LOGIN_REASON)
                    .tablePk(user.getId())
                    .tableName("user")
                    .build());

            experienceService.create(ExperienceDto.Create.builder()
                    .userId(user.getId())
                    .value(Experience.LOGIN_VALUE)
                    .reason(Experience.LOGIN_REASON)
                    .tablePk(user.getId())
                    .tableName("user")
                    .build());
            responseData.setSavedPoint(Point.LOGIN_VALUE);
        }

        return new MyResponse().setData(responseData);
    }

    @PostMapping("/join")
    public MyResponse join(@RequestBody AuthDto.Join dto) {
        service.findByEmail(dto.getEmail()).ifPresent(e -> {
            throw new DuplicateEmailException("duplicate email");
        });

        Long joinUser = service.create(UserDto.Create.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(encoder.encode(dto.getPwd()))
                .build());

        TokenDto token = jwtUtil.createToken(joinUser, true);
        AuthDto.JoinResponse responseData = AuthDto.JoinResponse.builder()
                .tokenType("Bearer")
                .access(token.getAccessToken())
                .savedPoint(Point.JOIN_VALUE)
                .build();

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + joinUser, token.getAccessToken(), JwtUtil.REFRESH_TOKEN_PERIOD, TimeUnit.SECONDS);
        return new MyResponse().setData(responseData);
    }

    @PostMapping("/addition")
    public MyResponse addition(HttpServletRequest request, @RequestBody AuthDto.Addition dto) {
        Long id = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        service.updateAddition(id, UserDto.UpdateAddition.builder().gender(dto.getGender()).age(dto.getAge()).tel(dto.getTel()).build());
        return new MyResponse();
    }

    // 인증번호 발송
    @PostMapping("/take")
    public MyResponse take(@RequestBody HashMap<String, String> map) throws Exception {
        String email = map.get("email");
        String code = mailUtil.generateAuthCode();
        mailUtil.sendMail(email, "인증코드", code);

        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(MAIL_PREFIX_KEY + email, code, 180, TimeUnit.SECONDS);
        return new MyResponse();
    }

    // 인증번호 확인
    @PostMapping("/certified")
    public MyResponse certified(@RequestBody AuthDto.Certified dto) throws Exception {
        ValueOperations<String, Object> vop = redis.opsForValue();
        String code = String.valueOf(vop.get(MAIL_PREFIX_KEY + dto.getEmail()));
        if (code == null) {
            throw new CertifiedException("invalid code");
        }

        if (!code.equals(dto.getCode())) {
            throw new CertifiedException("not matched code");
        }

        return new MyResponse();
    }

    // 인증번호 발송
    @PostMapping("/findpwd")
    public MyResponse findpwd(@RequestBody HashMap<String, String> map) throws Exception {
        String email = map.get("email");
        String code = mailUtil.generateAuthCode();
        mailUtil.sendMail(email, "인증코드", code);

        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(MAIL_PREFIX_KEY + email, code, 180, TimeUnit.SECONDS);

        AuthDto.FindPasswordResponse responseData = AuthDto.FindPasswordResponse.builder()
                .token(jwtUtil.createToken(email, 300))
                .build();

        return new MyResponse().setData(responseData);
    }

    @PostMapping("/resetpwd")
    public MyResponse resetpwd(@RequestBody AuthDto.Resetpwd dto) throws Exception {
        if (!jwtUtil.validateToken(dto.getToken(), true)) {
            throw new AuthFailException("invalid token");
        }

        Claims claims = jwtUtil.parseTokenString(dto.getToken());
        String email = claims.get("string", String.class);

        service.updatePwd(email, encoder.encode(dto.getPwd()));
        return new MyResponse();
    }

    @GetMapping("/logout")
    public MyResponse logout(HttpServletRequest request) {
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
        return new MyResponse();
    }

    // access token 리프레시
    @GetMapping("/refresh")
    public MyResponse refresh(HttpServletRequest request) throws Exception {
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
        return new MyResponse().setData(dataMap);
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

    private Authentication createAuthentication(Long id, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

}
