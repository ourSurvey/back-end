package com.oursurvey.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oursurvey.dto.MyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.oursurvey.dto.MyResponse.INVALID_CERTIFIED_CODE;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errorMessage = request.getAttribute(FilterException.JWT_ERROR.getErrorName()).toString();

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, new MyResponse().setCode(INVALID_CERTIFIED_CODE).setMessage(errorMessage));
            os.flush();
        }
    }
}
