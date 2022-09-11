package com.oursurvey;

import io.netty.handler.codec.DateFormatter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest {
    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int dayOfMonth = now.getDayOfMonth();
        System.out.println("year = " + year);
        System.out.println("monthValue = " + monthValue);
        String s = (monthValue / 10) > 0 ? ("" + monthValue) : ("0" + monthValue);
        System.out.println("s = " + s);
        System.out.println("dayOfMonth = " + dayOfMonth);

        String mm = now.format(DateTimeFormatter.ofPattern("MM"));
        System.out.println("mm = " + mm);

        String yyyyyy = now.format(DateTimeFormatter.ofPattern("yy"));
        System.out.println("yyyyyy = " + yyyyyy);
    }

    @Test
    void test2() {
        LocalDate now = LocalDate.now();
        String mm = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        System.out.println("mm = " + mm);

    }
}
