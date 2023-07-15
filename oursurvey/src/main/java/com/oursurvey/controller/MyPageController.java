package com.oursurvey.controller;

import com.oursurvey.dto.MyPageDto;
import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.security.AuthenticationParser;
import com.oursurvey.service.reply.ReplyService;
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
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyPageController {
    private final SurveyService surveyService;
    private final ReplyService replyService;

    @GetMapping("/survey")
    public MyResponse survey(@RequestParam(required = false) Integer status) {
        Long userId = AuthenticationParser.getIndex();

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

        int tempSurveyCount = surveyService.findTempByUserId(userId).size();
        int replyCount = replyService.findByUserId(userId).size();

        MyPageDto.SurveyResponse responseData = MyPageDto.SurveyResponse.builder()
                .tempCount(tempSurveyCount)
                .replyCount(replyCount)
                .willCount(surveyData.stream().filter(e -> e.getStatus().equals(-1)).count())
                .ingCount(surveyData.stream().filter(e -> e.getStatus().equals(0)).count())
                .finCount(surveyData.stream().filter(e -> e.getStatus().equals(1)).count())
                .list(status != null ? surveyData.stream().filter(e -> e.getStatus().equals(status)).toList() : surveyData)
                .build();

        return new MyResponse().setData(responseData);
    }

    @GetMapping("/survey/temp")
    public MyResponse surveyTemp() {
        Long userId = AuthenticationParser.getIndex();
        List<SurveyDto.MyListTemp> tempList = surveyService.findTempByUserId(userId);

        MyPageDto.SurveyTempResponse responseData = MyPageDto.SurveyTempResponse.builder()
                .tempList(tempList)
                .tempCount(tempList.size())
                .build();

        return new MyResponse().setData(responseData);
    }


    @GetMapping("/reply")
    public MyResponse reply(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Long userId = AuthenticationParser.getIndex();

        List<ReplyDto.MyList> replyList = replyService.findByUserId(userId);
        List<String> surveyIds = replyList.stream().map(ReplyDto.MyList::getSurveyId).toList();

        // 참여한 설문
        Page<SurveyDto.Lizt> surveyList = surveyService.find(PageRequest.of(page, size), "surveyIds", surveyIds);

        LocalDate now = LocalDate.now();
        Page<ReplyDto.MyReplyList> myReplyLists = surveyList.map(e -> {
            ReplyDto.MyReplyList.MyReplyListBuilder builder = ReplyDto.MyReplyList.builder()
                    .surveyId(e.getId())
                    .subject(e.getSubject())
                    .status(now.isAfter(e.getEndDate()) ? 1 : 0)
                    .openFl(e.getOpenFl())
                    .startDt(e.getStartDate())
                    .endDt(e.getEndDate());

            replyList.forEach(r -> {
                if (r.getSurveyId().equals(e.getId())) {
                    builder.replyId(r.getId());
                    builder.replyDate(r.getCreatedDt());
                }
            });

            return builder.build();
        });

        return new MyResponse().setData(myReplyLists);
    }
}
