package com.oursurvey.config.interceptor;

import com.oursurvey.exception.AuthFailException;
import com.oursurvey.exception.InvalidAccessTokenException;
import com.oursurvey.exception.InvalidRefreshTokenException;
import com.oursurvey.exception.InvalidTokenException;
import com.oursurvey.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private Environment env;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    RedisTemplate<String, Object> redis;

    @Value("${spring.redis.prefix.key}")
    private String REDIS_PREFIX_KEY;

    private final String REFRESH_TOKEN_URI = "/api/auth/refresh";

    // excludePathPatterns에서 method로 못잡을 경우 사용
    private final List<String> whiteList = Arrays.asList(
            // ex. GET:/api/example
            "GET:/api/survey"
    );

    private List<String> permit = List.of("active", "auth", "point", "survey", "hashtag", "reply", "file", "my");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        Optional<String> first = permit.stream().filter(requestUri::contains).findFirst();
        if (first.isEmpty()) {
            response.sendRedirect("/error");
            return false;
        }

        for (String element : whiteList) {
            if (element.startsWith(request.getMethod()) && element.split(":")[1].equals(requestUri)) {
                return true;
            }
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new AuthFailException("invalid header");
        }

        Boolean validateToken = jwtUtil.validateToken(authHeader);
        if (!validateToken) {
            throw new InvalidTokenException("invalid token");
        }

        Claims claims = jwtUtil.parseToken(authHeader);
        String tokenType = claims.get("tokenType", String.class);
        Long id = claims.get("id", Long.class);

        if (requestUri.equals(REFRESH_TOKEN_URI)) {
            if (tokenType.equals("access")) {
                throw new InvalidRefreshTokenException();
            }

            ValueOperations<String, Object> vop = redis.opsForValue();
            String refreshTokenInRedis = String.valueOf(vop.get(REDIS_PREFIX_KEY + id));
            if (!jwtUtil.extractToken(authHeader).equals(refreshTokenInRedis)) {
                throw new InvalidRefreshTokenException("not matched refresh token");
            }
        } else {
            if (tokenType.equals("refresh")) {
                throw new InvalidAccessTokenException();
            }
        }

        return true;
    }
}
