package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.*;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.service.answer.AnswerService;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.reply.ReplyService;
import com.oursurvey.service.survey.SurveyService;
import com.oursurvey.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final ReplyService replyService;
    private final AnswerService answerService;
    private final PointService pointService;

    private final JwtUtil jwtUtil;

    @SchemaMapping(typeName = "Query", value = "getSurveyToPage")
    public MyResponse getSurveyToPage(@Argument Integer page, @Argument Integer size, @Argument String searchText) {
        MyResponse res = new MyResponse();

        Page<SurveyDto.Lizt> lizts = surveyService.find(PageRequest.of(page, size), "searchText", searchText);
        lizts.map(SurveyDto.Lizt::convertHashtagListToList);

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("totalElements", lizts.getTotalElements());
        dataMap.put("totalPages", lizts.getTotalPages());
        dataMap.put("isLast", lizts.isLast());
        dataMap.put("currentPage", page);
        dataMap.put("content", lizts.getContent());
        return res.setData(dataMap);
    }

    @GetMapping("/{id}")
    public MyResponse get(@PathVariable String id) {
        MyResponse res = new MyResponse();
        Optional<SurveyDto.Detail> opt = surveyService.findById(id);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException("no survey");
        }

        return res.setData(opt.get());
    }

    @GetMapping("/tempCheck")
    public MyResponse beforeCreate(HttpServletRequest request) {
        MyResponse res = new MyResponse();
        HashMap<String, Object> dataMap = new HashMap<>();

        List<SurveyDto.MyListTemp> tempList = surveyService.findTempByUserId(jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION)));
        dataMap.put("tempCount", tempList.size());
        dataMap.put("tempRecent", tempList.size() > 0 ? tempList.get(0) : null);
        return res.setData(dataMap);
    }

    // NOTE. [point ++, experience ++]
    @PostMapping
    public MyResponse create(HttpServletRequest request, @RequestBody String json) {
        MyResponse res = new MyResponse();

        Long id = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        JSONObject object = new JSONObject(json);
        SurveyDto.Create surveyDto = getSurveyFromJson(object);
        surveyDto.setUserId(id);

        // save survey
        surveyService.create(surveyDto);
        return res;
    }

    // 설문 개별결과
    @GetMapping("/result/each/{surveyId}")
    public MyResponse resultEach(@PathVariable String surveyId) {
        MyResponse res = new MyResponse();

        HashMap<String, Object> dataMap = new HashMap<>();
        Optional<SurveyDto.Detail> opt = surveyService.findById(surveyId);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException("no survey");
        }
        dataMap.put("survey", opt.get());

        List<Long> replyIdList = replyService.findIdBySurveyId(surveyId);
        dataMap.put("replyIdList", replyIdList);

        if (replyIdList.size() > 0) {
            Long firstReplyId = replyIdList.get(0);
            List<AnswerDto.Base> answerList = answerService.findByReplyId(firstReplyId);
            dataMap.put("reply_" + firstReplyId, answerList.stream().collect(Collectors.toMap(AnswerDto.Base::getQuestionId, AnswerDto.Base::getResponse)));
        }

        return res.setData(dataMap);
    }

    // 설문 결과 요약
    @GetMapping("/result/summary/{surveyId}")
    public MyResponse result(@PathVariable String surveyId) {
        MyResponse res = new MyResponse();

        HashMap<String, Object> dataMap = new HashMap<>();
        Optional<SurveyDto.Detail> opt = surveyService.findById(surveyId);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException("no survey");
        }
        dataMap.put("survey", opt.get());

        ArrayList<QuestionDto.Summary> summaryList = new ArrayList<>();

        SurveyDto.Detail surveyDetail = opt.get();
        List<SectionDto.Detail> sectionList = surveyDetail.getSectionList();
        sectionList.forEach(section -> {
            section.getQuestionList().forEach(question -> {
                QuestionDto.Summary summaryElem = new QuestionDto.Summary(question.getId(), question.getMultiFl());
                if (question.getQuestionItemList().size() > 0) {
                    question.getQuestionItemList().forEach(item -> summaryElem.addToMap(item.getId(), 0));
                }
                summaryList.add(summaryElem);
            });
        });

        // question의 대한 answer 리스트들
        List<AnswerDto.Base> answerList = answerService.findByQuestionIds(summaryList.stream().map(QuestionDto.Summary::getId).toList());
        answerList.forEach(e -> {
            QuestionDto.Summary summary = summaryList.stream().filter(q -> q.getId().equals(e.getQuestionId())).findFirst().get();
            summary.setNoReplyFalse();
            HashMap<String, Integer> multiMap = summary.getMultiMap();

            if (summary.getMultiFl().equals(1)) {
                if (StringUtils.hasText(e.getResponse())) {
                    JSONArray responseArray = new JSONArray(e.getResponse());
                    for (Object o : responseArray) {
                        multiMap.replace(o.toString(), multiMap.get(o.toString()) + 1);
                        summary.plusAnswerCount();
                    }
                } else {
                    if (!multiMap.containsKey("noreply")) {
                        multiMap.put("noreply", 1);
                    } else {
                        multiMap.replace("noreply", (multiMap.get("noreply")) + 1);
                    }
                    summary.plusAnswerCount();
                }
            } else {
                if (StringUtils.hasText(e.getResponse())) {
                    summary.addToList(e.getResponse());
                } else {
                    summary.addToList("noreply");
                }

                summary.plusAnswerCount();
            }
        });

        // 퍼센트 계산
        for (QuestionDto.Summary s : summaryList) {
            if (s.getMultiFl().equals(1) && s.getAnswerCount() > 0) {
                for (Map.Entry<String, Integer> set : s.getMultiMap().entrySet()) {
                    s.getMultiMap().replace(set.getKey(), Math.round((float) set.getValue() / s.getAnswerCount() * 100));
                }
            }
        }
        dataMap.put("questionSummarys", summaryList);

        return res.setData(dataMap);
    }

    // NOTE. [point ++, experience ++]
    @GetMapping("/pull/{surveyId}")
    public MyResponse pull(HttpServletRequest request, @PathVariable String surveyId) {
        MyResponse res = new MyResponse();
        surveyService.pull(jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION)), surveyId);
        return res;
    }

    private SurveyDto.Create getSurveyFromJson(JSONObject object) {
        return SurveyDto.Create.builder()
                .id(object.getString("id"))
                .subject(object.getString("subject"))
                .content(object.getString("content"))
                .startDate(object.getString("startDate"))
                .endDate(object.getString("endDate"))
                .minute(object.getInt("minute"))
                .openFl(object.getInt("openFl"))
                .tempFl(object.getInt("tempFl"))
                .closingComment(object.getString("closingComment"))
                .hashtagList(getHashtag(object.getJSONArray("hashtag")))
                .sectionList(getSectionFromJson(object.getJSONArray("sections")))
                .build();
    }

    private List<String> getHashtag(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) list.add(jsonArray.getString(i));
        return list;
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
                    .descrip(question.getString("descrip"))
                    .multiFl(question.getInt("multiFl"))
                    .essFl(question.getInt("essFl"))
                    .dupFl(question.getInt("dupFl"))
                    .randomShowFl(question.getInt("randomShowFl"))
                    .nextFl(question.getInt("nextFl"))
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
