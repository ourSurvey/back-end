package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.*;
import com.oursurvey.entity.Point;
import com.oursurvey.exception.PointLackException;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.survey.SurveyService;
import com.oursurvey.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final PointService pointService;
    private final JwtUtil jwtUtil;

    @SchemaMapping(typeName = "Query", value = "getSurveyToPage")
    public MyResponse getSurveyToPage(@Argument Integer page, @Argument Integer size) {
        MyResponse res = new MyResponse();

        Page<SurveyDto.Lizt> lizts = surveyService.find(PageRequest.of(page, size));
        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("totalElements", lizts.getTotalElements());
        dataMap.put("totalPages", lizts.getTotalPages());
        dataMap.put("isLast", lizts.isLast());
        dataMap.put("content", lizts.getContent());
        return res.setData(dataMap);
    }

    // NOTE. [point -1000]
    @PostMapping
    public MyResponse create(HttpServletRequest request, @RequestBody String json) {
        MyResponse res = new MyResponse();

        Long id = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        Integer sumPoint = pointService.findSumByUserId(id);
        if (sumPoint < 1000) {
            throw new PointLackException("lack point");
        }

        JSONObject object = new JSONObject(json);
        SurveyDto.Create surveyDto = getSurveyFromJson(object);
        surveyDto.setUserId(id);

        // save survey
        Long surveyId = surveyService.create(surveyDto);

        // save point
        Integer survey = pointService.create(PointDto.Create.builder()
                .userId(id)
                .value(Point.CREATE_SURVEY_VALUE)
                .reason(Point.CREATE_SURVEY_REASON)
                .tablePk(surveyId)
                .tableName("survey")
                .build());

        return res;
    }

    private SurveyDto.Create getSurveyFromJson(JSONObject object) {
        return SurveyDto.Create.builder()
                .subject(object.getString("subject"))
                .content(object.getString("content"))
                .startDate(object.getString("startDate"))
                .endDate(object.getString("endDate"))
                .minute(object.getInt("minute"))
                .openFl(object.getInt("openFl"))
                .closingComment(object.getString("closingComment"))
                .sectionList(getSectionFromJson(object.getJSONArray("sections")))
                .build();
    }

    private List<SectionDto.Create> getSectionFromJson(JSONArray sectionList) {
        List<SectionDto.Create> res = new ArrayList<>();
        for (int i = 0; i < sectionList.length(); i++) {
            JSONObject section = sectionList.getJSONObject(i);
            res.add(SectionDto.Create.builder()
                    .title(section.getString("title"))
                    .content(section.getString("content"))
                    .nextSection(section.getLong("nextSection"))
                    .questionList(getQuestionDtoList(section.getJSONArray("questions")))
                    .build());
        }
        return res;
    }

    private List<QuestionDto.Create> getQuestionDtoList(JSONArray questionList) {
        List<QuestionDto.Create> questionDtoList = new ArrayList<>();
        for (int j = 0; j < questionList.length(); j++) {
            JSONObject question = questionList.getJSONObject(j);
            questionDtoList.add(QuestionDto.Create.builder()
                    .ask(question.getString("ask"))
                    .explain(question.getString("explain"))
                    .multiFl(question.getInt("multiFl"))
                    .essFl(question.getInt("essFl"))
                    .dupFl(question.getInt("dupFl"))
                    .oder(question.getInt("oder"))
                    .questionItemList(getQuestionItemDtoList(question.getJSONArray("questionItems")))
                    .build());
        }
        return questionDtoList;
    }

    private List<QuestionItemDto.Create> getQuestionItemDtoList(JSONArray questionItemList) {
        List<QuestionItemDto.Create> list = new ArrayList<>();
        for (int k = 0; k < questionItemList.length(); k++) {
            JSONObject questionItem = questionItemList.getJSONObject(k);
            list.add(QuestionItemDto.Create.builder()
                    .content(questionItem.getString("content"))
                    .oder(questionItem.getInt("oder"))
                    .nextSection(questionItem.getLong("nextSection"))
                    .build());
        }
        return list;
    }
}
