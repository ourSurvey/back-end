package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MyErrorController implements ErrorController {
    @RequestMapping(value = "/error")
    public MyResponse error() {
        return new MyResponse().setCode(MyResponse.NOT_FOUND).setMessage("not found or error");
    }
}
