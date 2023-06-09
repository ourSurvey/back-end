package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.service.point.PointService;
import com.oursurvey.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {
    private final PointService service;
    private final JwtUtil jwtUtil;

    @GetMapping
    public MyResponse get(HttpServletRequest request) {
        Long id = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        Integer sum = service.findSumByUserId(id);

        return new MyResponse().setData(sum);
    }
}
