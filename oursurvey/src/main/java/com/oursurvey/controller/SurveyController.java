package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.*;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.security.AuthenticationParser;
import com.oursurvey.jwt.JwtUtil;
import com.oursurvey.service.answer.AnswerService;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.reply.ReplyService;
import com.oursurvey.service.survey.SurveyService;
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

import static com.oursurvey.dto.repo.SurveyDto.*;

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
        Page<Lizt> lizts = surveyService.find(PageRequest.of(page, size), "searchText", searchText);
        return new MyResponse().setData(
                PageDto.builder()
                        .totalElements(lizts.getTotalElements())
                        .totalPages(lizts.getTotalPages())
                        .isLast(lizts.isLast())
                        .currentPage(page)
                        .content(lizts.getContent())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public MyResponse get(@PathVariable String id) {
        Detail survey = surveyService.findById(id).orElseThrow(() -> {
            throw new ObjectNotFoundException("no survey");
        });

        return new MyResponse().setData(survey);
    }

    @GetMapping("/tempCheck")
    public MyResponse beforeCreate(HttpServletRequest request) {
        Long userId = AuthenticationParser.getIndex();

        List<MyListTemp> tempList = surveyService.findTempByUserId(userId);
        return new MyResponse().setData(TempCheckDto.builder()
                .tempCount(tempList.size())
                .tempRecent(tempList.size() > 0 ? tempList.get(0) : null)
                .build());
    }

    // NOTE. [point ++, experience ++]
    @PostMapping
    public MyResponse create(@RequestBody String json) {
        Long userId = AuthenticationParser.getIndex();

        JSONObject object = new JSONObject(json);
        Create surveyDto = getSurveyFromJson(object);
        surveyDto.setUserId(userId);

        // save survey
        surveyService.create(surveyDto);
        return new MyResponse();
    }

    // 설문 개별결과
    @GetMapping("/result/each/{surveyId}")
    public MyResponse resultEach(@PathVariable String surveyId) {
        Detail surveyDetail = surveyService.findById(surveyId).orElseThrow(() -> {
            throw new ObjectNotFoundException("no survey");
        });
        List<Long> replyIdList = replyService.findIdBySurveyId(surveyId);

        ResultEachDto.ResultEachDtoBuilder builder = ResultEachDto.builder()
                .survey(surveyDetail)
                .replyIdList(replyIdList);

        if (replyIdList.size() > 0) {
            Long firstReplyId = replyIdList.get(0);
            List<AnswerDto.Base> answerList = answerService.findByReplyId(firstReplyId);
            Map<String, String> collect = answerList.stream().collect(Collectors.toMap(AnswerDto.Base::getQuestionId, AnswerDto.Base::getResponse));
            builder.firstReply(collect);
        }

        return new MyResponse().setData(builder.build());
    }

    // 설문 결과 요약
    @GetMapping("/result/summary/{surveyId}")
    public MyResponse result(@PathVariable String surveyId) {
        Detail surveyDetail = surveyService.findById(surveyId).orElseThrow(() -> {
            throw new ObjectNotFoundException("no survey");
        });


        ArrayList<QuestionDto.Summary> summaryList = new ArrayList<>();
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

        return new MyResponse().setData(
                ResultDto.builder()
                .survey(surveyDetail)
                .questionSummaryList(summaryList)
                .build());
    }

    // NOTE. [point ++, experience ++]
    @GetMapping("/pull/{surveyId}")
    public MyResponse pull(HttpServletRequest request, @PathVariable String surveyId) {
        surveyService.pull(jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION)), surveyId);
        return new MyResponse();
    }

    private Create getSurveyFromJson(JSONObject object) {
        return Create.builder()
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
