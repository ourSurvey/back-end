package com.oursurvey.dto;

import com.oursurvey.dto.repo.SurveyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class HomeDto {

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class Response {
        private List<SurveyDto.MyList> mySurvey;
        private Integer mySurveyCount;
        private Integer myPoint;
        private List<SurveyDto.Lizt> fiveMinuteSurvey;
        private List<SurveyDto.Lizt> openSurvey;
        private List<SurveyDto.Lizt> popularSurvey;
    }
}
