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
import com.oursurvey.entity.User;
import com.oursurvey.exception.*;
import com.oursurvey.jwt.TokenProvider;
import com.oursurvey.jwt.TokenType;
import com.oursurvey.repo.user.UserRepo;
import com.oursurvey.security.AuthenticationParser;
import com.oursurvey.service.experience.ExperienceService;
import com.oursurvey.service.loggedin.LoggedInService;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.user.UserService;
import com.oursurvey.jwt.JwtUtil;
import com.oursurvey.util.MailUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oursurvey.util.CustomValue.MAIL_PREFIX_KEY;
import static com.oursurvey.util.CustomValue.REDIS_PREFIX_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final PointService pointService;
    private final LoggedInService logService;
    private final ExperienceService experienceService;

    private final PasswordEncoder encoder;
    private final RedisTemplate<String, Object> redis;
    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MailUtil mailUtil;


    @PostMapping("/login")
    public MyResponse login(HttpServletRequest request, @RequestBody AuthDto.Login dto) throws Exception {
        UserDto.Basic user = userService.findByEmail(dto.getEmail());
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
                .refreshExpire(TokenType.REFRESH_TOKEN.getSecond())
                .sumPoint(sumPoint)
                .savedPoint(0)
                .build();

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + user.getId(), refreshToken, TokenType.REFRESH_TOKEN.getSecond(), TimeUnit.SECONDS);

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
        Optional<User> optional = userRepo.findByEmail(dto.getEmail());
        optional.ifPresent(user -> {
            throw new DuplicateEmailException("Already exist email");
        });

        Long joinUser = userService.create(UserDto.Create.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(encoder.encode(dto.getPwd()))
                .build());

        Authentication authentication = createAuthentication(joinUser, dto.getPwd());
        String accessToken = tokenProvider.createToken(authentication, TokenType.ACCESS_TOKEN);
        AuthDto.JoinResponse responseData = AuthDto.JoinResponse.builder()
                .tokenType("Bearer")
                .access(accessToken)
                .savedPoint(Point.JOIN_VALUE)
                .build();

        // redis
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set(REDIS_PREFIX_KEY + joinUser, accessToken, TokenType.ACCESS_TOKEN.getSecond(), TimeUnit.SECONDS);
        return new MyResponse().setData(responseData);
    }

    @PostMapping("/addition")
    public MyResponse addition(@RequestBody AuthDto.Addition dto) {
        Long userId = AuthenticationParser.getIndex();
        userService.updateAddition(userId, UserDto.UpdateAddition.builder()
                .gender(dto.getGender())
                .age(dto.getAge())
                .tel(dto.getTel())
                .build());

        return new MyResponse();
    }

    // 인증번호 발송
    @PostMapping("/take")
        public MyResponse take(@RequestBody AuthDto.EmailDto dto) throws Exception {
            UserDto.Basic user = userService.findByEmail(dto.getEmail());
            String email = user.getEmail();

            String code = mailUtil.generateAuthCode();
            mailUtil.sendMail(email, "인증코드", code);

            ValueOperations<String, Object> vop = redis.opsForValue();
            vop.set(MAIL_PREFIX_KEY + email, code, 180, TimeUnit.SECONDS);

            AuthDto.FindPasswordResponse responseData = AuthDto.FindPasswordResponse.builder()
                    .token(tokenProvider.createToken(email, TokenType.TOKEN_EXPIRE_5MINUTE))
                    .build();

        return new MyResponse().setData(responseData);
    }

    // 인증번호 확인
    @PostMapping("/certified")
    public MyResponse certified(@RequestBody AuthDto.Certified dto) throws Exception {
        ValueOperations<String, Object> vop = redis.opsForValue();
        String code = String.valueOf(vop.get(MAIL_PREFIX_KEY + dto.getEmail()));

        if (!StringUtils.hasText(code) || !code.equals(dto.getCode())) {
            throw new CertifiedException("Invalid or Not Matched Code");
        }

        return new MyResponse();
    }

    @PostMapping("/resetpwd")
    public MyResponse resetpwd(@RequestBody AuthDto.Resetpwd dto) throws Exception {
        if (!tokenProvider.validateToken(dto.getToken())) {
            throw new AuthFailException("invalid token");
        }

        Claims claims = tokenProvider.parseToken(dto.getToken());
        String email = claims.get("string", String.class);

        userService.updatePwd(email, encoder.encode(dto.getPwd()));
        return new MyResponse();
    }

    @GetMapping("/logout")
    public MyResponse logout(HttpServletRequest request) {
        String token = tokenProvider.resolveHeader(request);
        if (token == null) {
            throw new InvalidTokenException();
        }

        if (!tokenProvider.validateToken(token)) {
            throw new InvalidRefreshTokenException();
        }

        Claims claims = tokenProvider.parseToken(token);
        String tokenType = claims.get("tokenType", String.class);
        if (!tokenType.equals("refresh")) {
            throw new InvalidRefreshTokenException("not refresh token");
        }

        Long id = Long.parseLong(claims.getSubject());
        String redisKey = REDIS_PREFIX_KEY + id;
        ValueOperations<String, Object> vop = redis.opsForValue();
        if (!Objects.equals(vop.get(redisKey), token)) {
            throw new InvalidRefreshTokenException();
        }

        request.getSession().invalidate();
        vop.set(REDIS_PREFIX_KEY + id, token, 1, TimeUnit.MILLISECONDS);
        return new MyResponse();
    }

    // access token 리프레시
    @GetMapping("/refresh")
    public MyResponse refresh(HttpServletRequest request) throws Exception {
        String token = tokenProvider.resolveHeader(request);
        if (token == null) {
            throw new InvalidTokenException();
        }

        boolean validate = tokenProvider.validateToken(token);
        if (!validate) {
            throw new InvalidTokenException();
        }

        Claims claims = tokenProvider.parseToken(token);
        String tokenType = claims.get("tokenType", String.class);
        if (!tokenType.equals("refresh")) {
            throw new InvalidRefreshTokenException();
        }

        Long id = Long.parseLong(claims.getSubject());
        Authentication authentication = createAuthentication(id, "");
        String access = tokenProvider.createToken(authentication, TokenType.ACCESS_TOKEN);
        TokenDto.Response response = TokenDto.Response.builder()
                .tokenType("Bearer")
                .access(access)
                .build();

        return new MyResponse().setData(response);
    }

    @GetMapping("/validate")
    public MyResponse validate(HttpServletRequest request) throws Exception {
        MyResponse res = new MyResponse();
        String header = tokenProvider.resolveHeader(request);
        if (header == null) {
            return res.setCode(MyResponse.INVALID_TOKEN).setData(false);
        }

        if (!tokenProvider.validateToken(header)) {
            return res.setCode(MyResponse.INVALID_TOKEN).setData(false);
        }

        Claims claims = tokenProvider.parseToken(header);
        String tokenType = claims.get("tokenType", String.class);
        if (!tokenType.equals("access")) {
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
