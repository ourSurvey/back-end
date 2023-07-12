package com.oursurvey;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomTest {
    @Test
    void test() {
        String code = "";
        Random random = new Random();
        for(int i = 0; i < 6; i++) {
            code += random.nextInt(9);
        }

        System.out.println("code = " + code);
    }
}
