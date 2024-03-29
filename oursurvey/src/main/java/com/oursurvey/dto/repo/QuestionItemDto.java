package com.oursurvey.dto.repo;

import lombok.*;

public class QuestionItemDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String content;
        private Integer oder;
        private Long nextSection;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private String id;
        private String content;
        private Integer oder;
        private Long nextSection;
    }
}
