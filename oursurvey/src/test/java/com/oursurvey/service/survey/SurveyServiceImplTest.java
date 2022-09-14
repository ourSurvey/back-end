package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.repo.hashtagsurvey.HashtagSurveyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SurveyServiceImplTest {
    @Autowired
    SurveyService service;
    @Autowired
    HashtagSurveyRepo repo;

    @Test
    void test() {
        Page<SurveyDto.Lizt> list = service.find(PageRequest.of(0, 10));
        List<SurveyDto.Lizt> content = list.getContent();
        System.out.println("content = " + content);
    }

    @Test
    void test1111() {
        List<SurveyDto.MyList> list = service.findByUserId(11L);

        LocalDate now = LocalDate.now();

        list.forEach(e -> {
            if (now.isBefore(e.getStartDate())) {
                e.setStatus(-1);
            } else if(now.isAfter(e.getEndDate())) {
                e.setStatus(1);
            } else {
                e.setStatus(0);
            }
            System.out.println("e = " + e);
        });
    }
}