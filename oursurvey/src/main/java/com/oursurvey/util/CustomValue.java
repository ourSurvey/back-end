package com.oursurvey.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomValue {
    public static String REDIS_PREFIX_KEY;

    public static String MAIL_PREFIX_KEY;

    @Value("${custom.redis.prefix.key}")
    public void setRedisPrefixKey(String redisPrefixKey) {
        REDIS_PREFIX_KEY = redisPrefixKey;
    }

    @Value("${custom.mail.prefix.key}")
    public void setMailPrefixKey(String mailPrefixKey) {
        MAIL_PREFIX_KEY = mailPrefixKey;
    }
}
