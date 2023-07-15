package com.oursurvey.service.point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceTest {

    @Autowired
    PointService pointService;

    @Test
    void test() {
        Integer integer = pointService.findSumByUserId(1L);
        System.out.println("integer = " + integer);
    }
}