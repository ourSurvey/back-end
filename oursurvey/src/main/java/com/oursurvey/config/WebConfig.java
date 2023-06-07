package com.oursurvey.config;

import com.oursurvey.config.interceptor.AuthInterceptor;
import com.oursurvey.config.resolver.IndexInfoResolver;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final IndexInfoResolver indexInfoResolver;

    @Bean
    @Primary
    PasswordEncoder getBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    JPAQueryFactory getJPAQueryFactory(EntityManager em) {;
        return new JPAQueryFactory(em);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/*.ico",
                        "/error",
                        "/index.html",
                        "/",
                        "/null",
                        "/api/test/**",        // 테스트
                        "/api/auth/login",     // 로그인
                        "/api/auth/logout",    // 로그아웃
                        "/api/auth/join",      // 가입
                        "/api/auth/take",      // 인증번호 전송
                        "/api/auth/certified", // 인증번호 확인
                        "/api/auth/findpwd",   // 비밀번호 찾기
                        "/api/auth/resetpwd",  // 비밀번호 초기화
                        "/api/auth/refresh",   // access 토큰 재발급
                        "/api/auth/validate",  // access 토큰 검증
                        "/api/reply",          // 설문 답변
                        "/graphql",
                        "/graphiql"
                );
    }

    @Bean
    AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "*"
                )
                .allowedHeaders(
                        "*"
                )
                .allowedMethods(
                        "*"
                );
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // SortHandlerMethodArgumentResolver sortArgumentResolver = new SortHandlerMethodArgumentResolver();
        // sortArgumentResolver.setSortParameter("sortBy");
        // sortArgumentResolver.setPropertyDelimiter("-");
        // PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver(sortArgumentResolver);
        // pageableArgumentResolver.setOneIndexedParameters(true);
        // pageableArgumentResolver.setMaxPageSize(20);
        // pageableArgumentResolver.setFallbackPageable(PageRequest.of(0,10));
        // resolvers.add(pageableArgumentResolver);
        resolvers.add(indexInfoResolver);
    }
}
