package com.oursurvey.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM;

    public void sendMail(String to, String subject, String content) throws Exception {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
        helper.setFrom(FROM);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mail);
    }

    public String generateAuthCode() {
        String code = "";
        Random random = new Random();
        for(int i = 0; i < 6; i++) {
            code += random.nextInt(9);
        }

        return code;
    }
}
