package com.oursurvey;

import com.oursurvey.util.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {
    @Autowired
    MailUtil mailUtil;

    @Test
    void test() throws Exception {
        // mailUtil.sendMail("eikhyeon8542@naver.com", "111", "2222");
    }
}
