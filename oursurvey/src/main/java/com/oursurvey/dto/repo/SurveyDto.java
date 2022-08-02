package com.oursurvey.dto.repo;

import lombok.*;

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
}
