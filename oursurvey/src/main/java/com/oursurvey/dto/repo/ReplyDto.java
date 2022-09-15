package com.oursurvey.dto.repo;

import lombok.*;

import java.util.List;

public class ReplyDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long userId;
        private String surveyId;
        private List<AnswerDto.Create> answerList;

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyList {
        private Long id;

        public MyList(Long id) {
            this.id = id;
        }
    }
}
