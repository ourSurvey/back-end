package com.oursurvey.dto;

import com.oursurvey.dto.repo.SurveyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MyPageDto {
    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class SurveyResponse {
        private Integer tempCount;
        private Integer replyCount;
        private Long willCount;
        private Long ingCount;
        private Long finCount;
        private List<SurveyDto.MyList> list;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class SurveyTempResponse {
        private List<SurveyDto.MyListTemp> tempList;
        private Integer tempCount;
    }
}
