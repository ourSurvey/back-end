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
        private Long id;
        private Long userId;
        private String subject;
        private String content;
        private String startDate;
        private String endDate;
        private Integer minute;
        private Integer openFl;
        private Integer tempFl;
        private String closingComment;
        private List<String> hashtagList;
        private List<SectionDto.Create> sectionList;
        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private String nickname;
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
        private List<String> hashtagList;
        private List<SectionDto.Detail> sectionList;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Lizt {
        private Long id;
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
        private Object hashtagList;

        public Lizt convertHashtagListToList() {
            String a = (String) hashtagList;
            if (a != null) {
                this.hashtagList = List.of(a.split(","));
            }

            return this;
        }
    }
}
