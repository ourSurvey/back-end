package com.oursurvey.dto.repo;

import lombok.*;

import java.util.List;

public class SectionDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String title;
        private String content;
        private Long nextSection;
        private List<QuestionDto.Create> questionList;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private String id;
        private String title;
        private String content;
        private Long nextSection;
        private List<QuestionDto.Detail> questionList;
    }
}
