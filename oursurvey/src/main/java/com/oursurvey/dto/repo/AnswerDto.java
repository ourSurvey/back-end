package com.oursurvey.dto.repo;

import lombok.*;

public class AnswerDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String questionId;
        private String value;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Base {
        private Long id;
        private Long replyId;
        private String questionId;
        private String response;
    }
}
