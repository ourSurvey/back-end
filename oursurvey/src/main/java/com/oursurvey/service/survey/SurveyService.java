package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.SurveyDto;

public interface SurveyService {
    Long create(SurveyDto.Create dto);
}
