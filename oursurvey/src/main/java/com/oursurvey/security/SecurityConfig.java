package com.oursurvey.security;

import com.oursurvey.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder getBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()

                .headers().frameOptions().disable()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())

                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(StringUtils.toStringArray(getPermitAllList())).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .formLogin().disable()
                .logout().disable()
        ;

        return http.build();
    }

    private List<String> getPermitAllList() {
        return List.of(
                "/",
                "/api/auth/logout",
                "/api/auth/refresh",
                "/api/auth/validate",
                "/api/active",
                "/api/auth/login",
                "/api/auth/join",
                "/api/auth/send-mail",
                "/api/auth/certified",
                "/api/auth/findpwd",
                "/api/auth/resetpwd",
                "/api/reply"
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration conf = new CorsConfiguration();
        List<String> allowedOrigins = List.of(
                "*"
        );

        conf.setAllowedOriginPatterns(allowedOrigins);
        conf.addAllowedHeader("*");
        conf.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS", "PUT"));
        conf.setMaxAge(3600L);
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }


    // @Bean // permitAll을 사용할 것
    // public WebSecurityCustomizer webSecurityCustomizer() {
    //     return (web) -> web.ignoring()
    //             .antMatchers(HttpMethod.GET, MethodIgnore.GET_IGNORE.toStringArray())
    //             .antMatchers(HttpMethod.POST, MethodIgnore.POST_IGNORE.toStringArray())
    //             .antMatchers(HttpMethod.PUT, MethodIgnore.PUT_IGNORE.toStringArray())
    //             .antMatchers(HttpMethod.PATCH, MethodIgnore.PATCH_IGNORE.toStringArray())
    //             .antMatchers(HttpMethod.DELETE, MethodIgnore.DELETE_IGNORE.toStringArray());
    // }
}
