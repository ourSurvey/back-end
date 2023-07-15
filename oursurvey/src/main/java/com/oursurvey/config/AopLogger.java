package com.oursurvey.config;

import graphql.com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class AopLogger {
    @Pointcut("within(com.oursurvey.controller..*)")
    public void onRequest() { }

    @Around("onRequest()")
    public Object traceLog(ProceedingJoinPoint point) throws Throwable {
        Object result = null;

        String declareClass = point.getSignature().toShortString();
        try {
            // request
            this.printRequestLog(point, declareClass);

            result = point.proceed(point.getArgs());

            // response
            this.printResponseLog(declareClass, result);

            return result;
        } catch (Throwable throwable) {
            printExceptionLog(declareClass, throwable);
            throw throwable;
        }
    }

    private void printRequestLog(ProceedingJoinPoint point, String declareClass) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }

        log.info("{} Request -> {} {}, params = {}, body = {}", declareClass, request.getMethod(), request.getRequestURI(), params, point.getArgs());
    }

    private void printResponseLog(String declareClass, Object result) {
        log.info("{} Response -> {} ", declareClass, result);
    }

    private void printExceptionLog(String declareClass, Throwable throwable) {
        log.error("{}, Exception = {}", declareClass, throwable.getMessage());

    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
