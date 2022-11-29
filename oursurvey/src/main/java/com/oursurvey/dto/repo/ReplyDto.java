package com.oursurvey.dto.repo;

import lombok.*;

import java.time.LocalDateTime;
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
        private String surveyId;
        private LocalDateTime createdDt;

        public MyList(Long id, String surveyId, LocalDateTime createdDt) {
            this.id = id;
            this.surveyId = surveyId;
            this.createdDt = createdDt;
        }
    }
}
