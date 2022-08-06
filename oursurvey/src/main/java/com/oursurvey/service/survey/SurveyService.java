package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyService {
    Long create(SurveyDto.Create dto);

    Page<SurveyDto.Lizt> find(Pageable pageable);
}
