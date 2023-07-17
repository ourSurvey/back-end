package com.oursurvey.security;

import com.oursurvey.exception.AuthFailException;
import com.oursurvey.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveHeader(request);
        log.info("@@ JwtFilter URI = {}, token = {}", request.getRequestURI(), token);

        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // token이 필요한 api에 토큰이 없거나 유효하지 않는 경우 에러 발생 CustomAuthenticationEntryPoint
            // filter에서 직접 throw new exception을 할 수 없으니 위임한다
            request.setAttribute(FilterException.JWT_ERROR.getErrorName(), "Invalid Token");
        }

        filterChain.doFilter(request,response);
    }


    private String resolveHeader(HttpServletRequest request) {
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
