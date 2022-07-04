package com.oursurvey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, Object> redis;
    
    @Test
    void test() {
        ValueOperations<String, Object> vop = redis.opsForValue();
        vop.set("tempKey", "tempValue");
    }
}
