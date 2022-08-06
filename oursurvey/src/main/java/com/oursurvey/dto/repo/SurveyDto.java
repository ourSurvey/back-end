package com.oursurvey.dto.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SurveyDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long userId;
        private String subject;
        private String content;
        private String startDate;
        private String endDate;
        private Integer minute;
        private Integer openFl;
        private String closingComment;
        private List<SectionDto.Create> sectionList;

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Lizt {
        private String subject;
        private String content;
        private Integer openFl;
        private Integer minute;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate endDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDateTime createdDt;
        private String hashtagList;
    }
}
