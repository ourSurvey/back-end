package com.oursurvey.controller;

import com.oursurvey.dto.HomeDto;
import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.jwt.JwtUtil;
import com.oursurvey.security.AuthenticationParser;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.survey.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final SurveyService surveyService;
    private final PointService pointService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public MyResponse get(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        LocalDate now = LocalDate.now();

        Long userId = AuthenticationParser.getIndex();
        System.out.println("userId = " + userId);

        List<SurveyDto.MyList> mySurvey = surveyService.findByUserId(userId);
        mySurvey.forEach(e -> {
            if (now.isBefore(e.getStartDate())) {
                e.setStatus(-1);
            } else if (now.isAfter(e.getEndDate())) {
                e.setStatus(1);
            } else {
                e.setStatus(0);
            }
        });

        Integer myPoint = pointService.findSumByUserId(userId);
        Page<SurveyDto.Lizt> minute = surveyService.find(PageRequest.of(0, 25), "minute", 5);
        Page<SurveyDto.Lizt> openFl = surveyService.find(PageRequest.of(0, 25), "openFl", 1);
        Page<SurveyDto.Lizt> viewCnt = surveyService.find(PageRequest.of(0, 25), "viewCnt", null);

        HomeDto.Response responseData = HomeDto.Response.builder()
                .mySurvey((mySurvey.size() > 3) ? mySurvey.subList(0, 3) : mySurvey)
                .mySurveyCount(mySurvey.size())
                .myPoint(myPoint)
                .fiveMinuteSurvey(minute.getContent())
                .openSurvey(openFl.getContent())
                .popularSurvey(viewCnt.getContent())
                .build();

        return new MyResponse().setData(responseData);
    }
}
