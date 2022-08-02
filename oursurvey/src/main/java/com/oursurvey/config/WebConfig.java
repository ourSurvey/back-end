package com.oursurvey.config;

import com.oursurvey.config.interceptor.AuthInterceptor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
                        "/api/test/**",
                        "/api/auth/login",
                        "/api/auth/logout",
                        "/api/auth/join",
                        "/api/auth/take",
                        "/api/auth/certified",
                        "/api/auth/findpwd",
                        "/api/auth/resetpwd",
                        "/api/auth/refresh"
                );
    }

    @Bean
    AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
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
}
