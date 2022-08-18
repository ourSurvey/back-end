package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.service.hashtag.HashtagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hashtag")
@RequiredArgsConstructor
public class HashtagController {
    private final HashtagService service;

    @GetMapping("/{value}")
    public MyResponse get(@PathVariable String value) {
        return new MyResponse().setData(service.findListByValue(value));
    }
}
