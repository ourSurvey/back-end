package com.oursurvey.dto.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurveyDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String id;
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
        private String id;
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
        private String id;
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

    // 마이페이지 나의 서베이
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyList {
        private String id;
        private String subject;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDate endDate;
        private Integer tempFl;
        private Long replyCount;
        private Integer status;

        public MyList(String id, String subject, LocalDate startDate, LocalDate endDate, Integer tempFl, Long replyCount) {
            this.id = id;
            this.subject = subject;
            this.startDate = startDate;
            this.endDate = endDate;
            this.tempFl = tempFl;
            this.replyCount = replyCount;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    // 마이페이지 나의 서베이 임시저장
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MyListTemp {
        private String id;
        private String subject;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        private LocalDateTime createdDt;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class TempCheckDto {
        private Integer tempCount;
        private MyListTemp tempRecent;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class ResultEachDto {
        private Detail survey;
        private List<Long> replyIdList;
        private Map<String, String> firstReply;
    }


    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class ResultDto {
        private Detail survey;
        private ArrayList<QuestionDto.Summary> questionSummaryList;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class PageDto {
        private Long totalElements;
        private Integer totalPages;
        private Boolean isLast;
        private Integer currentPage;
        private List<Lizt> content;
    }
}
