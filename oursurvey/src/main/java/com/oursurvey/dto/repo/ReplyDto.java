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
        private Long surveyId;
        private List<AnswerDto.Create> answerList;

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}