package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.service.reply.ReplyService;
import com.oursurvey.service.survey.SurveyService;
import com.oursurvey.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {
    private final SurveyService surveyService;
    private final ReplyService replyService;

    private final JwtUtil jwtUtil;

    @GetMapping("/survey")
    public MyResponse survey(HttpServletRequest request, @RequestParam(required = false) Integer status) {
        MyResponse res = new MyResponse();

        Long userId = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));

        LocalDate now = LocalDate.now();
        List<SurveyDto.MyList> surveyData = surveyService.findByUserId(userId);

        surveyData.forEach(e -> {
            if (now.isBefore(e.getStartDate())) {
                e.setStatus(-1);
            } else if (now.isAfter(e.getEndDate())) {
                e.setStatus(1);
            } else {
                e.setStatus(0);
            }
        });

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tempCount", surveyService.findTempByUserId(userId).size());
        dataMap.put("finCount", surveyData.stream().filter(e -> e.getStatus().equals(-1)).count());
        dataMap.put("ingCount", surveyData.stream().filter(e -> e.getStatus().equals(0)).count());
        dataMap.put("willCount", surveyData.stream().filter(e -> e.getStatus().equals(1)).count());
        dataMap.put("replyCount", replyService.findByUserId(userId).size());
        dataMap.put("list", status != null ? surveyData.stream().filter(e -> e.getStatus().equals(status)).toList() : surveyData);
        return res.setData(dataMap);
    }

    @GetMapping("/survey/temp")
    public MyResponse surveyTemp(HttpServletRequest request) {
        MyResponse res = new MyResponse();
        HashMap<String, Object> dataMap = new HashMap<>();

        List<SurveyDto.MyListTemp> tempList = surveyService.findTempByUserId(jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION)));
        dataMap.put("tempList", tempList);
        dataMap.put("tempCount", tempList.size());
        return res.setData(dataMap);
    }


    @GetMapping("/reply")
    public MyResponse reply() {
        MyResponse res = new MyResponse();
        return res;
    }
}
