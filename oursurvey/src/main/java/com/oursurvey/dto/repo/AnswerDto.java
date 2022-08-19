package com.oursurvey.dto.repo;

import lombok.*;

public class AnswerDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long questionId;
        private String value;
    }
}
