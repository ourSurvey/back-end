package com.oursurvey.dto.repo;

import lombok.*;

import java.time.LocalDateTime;

public class LoggedInDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Base {
        private Long id;
        private Long userId;
        private String remoteAddr;
        private LocalDateTime createdDt;
    }


    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long userId;
        private String remoteAddr;
    }
}
