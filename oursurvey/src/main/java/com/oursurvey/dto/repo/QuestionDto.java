package com.oursurvey.dto.repo;

import lombok.*;

import java.util.List;

public class QuestionDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String ask;
        private String descrip;
        private Integer multiFl;
        private Integer essFl;
        private Integer dupFl;
        private Integer oder;
        private List<QuestionItemDto.Create> questionItemList;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private String ask;
        private String descrip;
        private Integer multiFl;
        private Integer essFl;
        private Integer dupFl;
        private Integer oder;
        private List<QuestionItemDto.Detail> questionItemList;
    }
}
