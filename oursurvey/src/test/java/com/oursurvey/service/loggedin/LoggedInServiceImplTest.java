package com.oursurvey.service.loggedin;

import com.oursurvey.dto.repo.LoggedInDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoggedInServiceImplTest {

    @Autowired
    LoggedInService service;

    @Test
    void test() {
        Optional<LoggedInDto.Base> byUserIdDate = service.findByUserIdDate(1L, "2022-08-06");
    }

}