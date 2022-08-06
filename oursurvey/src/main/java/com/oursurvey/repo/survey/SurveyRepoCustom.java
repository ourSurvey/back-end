package com.oursurvey.repo.survey;

import com.oursurvey.dto.repo.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyRepoCustom {
    Page<SurveyDto.Lizt> get(Pageable pageable);
}
