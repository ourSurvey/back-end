package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.reply.ReplyService;
import com.oursurvey.service.survey.SurveyService;
import com.oursurvey.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final ReplyService replyService;
    private final SurveyService surveyService;
    private final PointService pointService;

    private final JwtUtil jwtUtil;

    @GetMapping
    public MyResponse get(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        MyResponse res = new MyResponse();

        LocalDate now = LocalDate.now();
        HashMap<String, Object> dataMap = new HashMap<>();
        Long userId = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));

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

        // NOTE. 3개 이상일 때 기획!
        dataMap.put("mySurvey", (mySurvey.size() > 3) ? mySurvey.subList(0, 3) : mySurvey);
        dataMap.put("mySurveyCount", mySurvey.size());

        // NOTE. 내 합산 포인트
        Integer myPoint = pointService.findSumByUserId(userId);
        dataMap.put("myPoint", myPoint);

        // NOTE. 5분이하 설문들
        Page<SurveyDto.Lizt> minute = surveyService.find(PageRequest.of(0, 25), "minute", 5);
        dataMap.put("fiveMinuteSurvey", minute.getContent());

        // NOTE. 진행중 + 공개
        Page<SurveyDto.Lizt> openFl = surveyService.find(PageRequest.of(0, 25), "openFl", 1);
        dataMap.put("openSurvey", openFl.getContent());

        // NOTE. 진행중 + 조회수 많은 순
        Page<SurveyDto.Lizt> viewCnt = surveyService.find(PageRequest.of(0, 25), "viewCnt", null);
        dataMap.put("popularSurvey", openFl.getContent());


        return res.setData(dataMap);
    }
}
