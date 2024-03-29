package com.oursurvey.jwt;

import com.oursurvey.dto.TokenDto;
import com.oursurvey.exception.AuthFailException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@Deprecated
public class JwtUtil {
    @Value("${custom.jwt.key}")
    private String jwtKey;
    private Key key;
    private static final int ACCESS_TOKEN_PERIOD = 60 * 60 * 3; // 3 hour

    public static final int REFRESH_TOKEN_PERIOD = 60 * 60 * 24 * 7; // 7 days

    @PostConstruct
    private void createKey() {
        this.key = Keys.hmacShaKeyFor(this.jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenDto createToken(Long id, Boolean onlyAccess) {
        TokenDto tokenDto = new TokenDto();
        long time = new Date().getTime();

        tokenDto.setAccessToken(Jwts.builder()
                .setSubject("accessToken")
                .claim("id", id)
                .claim("tokenType", "access")
                .setExpiration(new Date(System.currentTimeMillis() + (REFRESH_TOKEN_PERIOD * 1000)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact());

        if (!onlyAccess) {
            tokenDto.setRefreshToken(Jwts.builder()
                    .setSubject("refreshToken")
                    .claim("id", id)
                    .claim("tokenType", "refresh")
                    .setExpiration(new Date(System.currentTimeMillis() + (REFRESH_TOKEN_PERIOD * 1000)))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact());

            tokenDto.setRefreshTokenExpire(REFRESH_TOKEN_PERIOD);
        }

        return tokenDto;
    }

    public TokenDto createToken(Long id) {
        return createToken(id, false);
    }

    public String createToken(String str, Integer sec) {
        long time = new Date().getTime();
        return Jwts.builder().setSubject("stringToken")
                .claim("string", str)
                .setExpiration(new Date(time + (sec * 1000)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Boolean validateToken(String header, Boolean justString) {
        String token = justString ? header : this.extractToken(header);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            // log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            // log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            // log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    public Boolean validateToken(String header) {
        return this.validateToken(header, false);
    }

    public Claims parseToken(String header) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(this.extractToken(header)).getBody();
    }

    public Claims parseTokenString(String string) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(string).getBody();
    }

    public Long getLoginUserId(String header) {
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(this.extractToken(header)).getBody();
        return body.get("id", Long.class);
    }

    public String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthFailException("invalid jwt token(extract)");
        }

        return header.substring("Bearer ".length());
    }
}
