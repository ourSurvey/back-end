package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.repo.hashtagsurvey.HashtagSurveyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    void test2() {
        Optional<SurveyDto.Detail> opt = service.findById(12L);
        System.out.println("opt = " + opt);
    }

    @Test
    void test3() {
        List<HashtagDto.Base> bySurveyId = repo.getBySurveyId(12L);
        System.out.println("bySurveyId = " + bySurveyId);
    }
}