package com.oursurvey.dto.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
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

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MyReplyList {
        private String surveyId;
        private Long replyId;
        private String subject;
        private Integer status;
        private Integer openFl;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate startDt;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate endDt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDateTime replyDate;
    }
}
