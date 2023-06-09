package com.oursurvey.tdd;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Test
    void passwordEncoder() {
        String password = "1234";
        String encode = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, encode);
        assertThat(matches).isTrue();
        log.debug("encode = {}", encode);
    }
}
