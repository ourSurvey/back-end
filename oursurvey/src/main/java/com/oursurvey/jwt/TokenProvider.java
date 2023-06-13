package com.oursurvey.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private String jwtKey;
    private Key key;

    public TokenProvider(@Value("${custom.jwt.key}") String jwtKey) {
        this.jwtKey = jwtKey;
    }

    @PostConstruct
    public void setKey() {
        this.key = Keys.hmacShaKeyFor(this.jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication auth, TokenType tokenType) {
        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = Date.from(LocalDateTime.now().plusHours(tokenType.getSecond())
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("auth", authorities)
                .claim("tokenType", tokenType.getType())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
}

    public String createToken(String string, TokenType tokenType) {
        Date validity = Date.from(LocalDateTime.now().plusHours(tokenType.getSecond())
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .claim("string", string)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return !body.isEmpty();
        } catch (SecurityException e) {
            throw new RuntimeException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT token compact of handler are invalid");
        } catch (Exception e) {
            throw new RuntimeException("Unknown Jwt token exception");
        }
    }

    public String resolveHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header)) {
            return null;
        }

        if (header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length());
        }

        return header;
    }
}
