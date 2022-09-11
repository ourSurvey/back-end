package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.AnswerDto;
import com.oursurvey.dto.repo.PointDto;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.entity.Point;
import com.oursurvey.service.point.PointService;
import com.oursurvey.service.reply.ReplyService;
import com.oursurvey.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService service;
    private final PointService pointService;
    private final JwtUtil jwtUtil;

    // NOTE. [point ++, experience ++]
    @PostMapping
    public MyResponse post(HttpServletRequest request, @RequestBody String json) {
        MyResponse res = new MyResponse();

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Long loginUserId = null;
        if (StringUtils.hasText(header)) {
             loginUserId = jwtUtil.getLoginUserId(header);
        }

        ReplyDto.Create dto = getReplyFromJson(new JSONObject(json));
        dto.setUserId(loginUserId);

        // save reply
        service.create(dto);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("savedPoint", loginUserId != null ? Point.REPLY_SURVEY_VALUE : 0);
        return res.setData(map);
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
