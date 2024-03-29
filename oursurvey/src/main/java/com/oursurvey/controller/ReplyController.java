package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.AnswerDto;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.entity.Point;
import com.oursurvey.jwt.TokenProvider;
import com.oursurvey.service.answer.AnswerService;
import com.oursurvey.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService service;
    private final AnswerService answerService;
    private final TokenProvider tokenProvider;

    @GetMapping("/{id}")
    public MyResponse get(@PathVariable Long id) {
        MyResponse res = new MyResponse();
        List<AnswerDto.Base> answerList = answerService.findByReplyId(id);
        Map<String, String> collect = answerList.stream().collect(Collectors.toMap(AnswerDto.Base::getQuestionId, AnswerDto.Base::getResponse));

        return res.setData(collect);
    }

    // NOTE. [point ++, experience ++]
    @PostMapping
    public MyResponse post(HttpServletRequest request, @RequestBody String json) {
        String token = tokenProvider.resolveHeader(request);
        Long userId = null;
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            userId = Long.parseLong(tokenProvider.parseToken(token).getSubject());
        }

        ReplyDto.Create dto = getReplyFromJson(new JSONObject(json));
        dto.setUserId(userId);

        // save reply
        service.create(dto);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("savedPoint", userId != null ? Point.REPLY_SURVEY_VALUE : 0);
        return new MyResponse().setData(map);
    }

    private ReplyDto.Create getReplyFromJson(JSONObject j) {
        return ReplyDto.Create.builder()
                .userId(1L)
                .surveyId(j.getString("surveyId"))
                .answerList(getAnswerFromJson(j.getJSONArray("answers")))
                .build();
    }

    private List<AnswerDto.Create> getAnswerFromJson(JSONArray j) {
        List<AnswerDto.Create> result = new ArrayList<>();
        for (int i = 0; i < j.length(); i++) {
            JSONObject obj = j.getJSONObject(i);
            result.add(AnswerDto.Create.builder()
                    .questionId(obj.getString("questionId"))
                    .value(obj.getString("value"))
                    .build());
        }
        return result;
    }
}
