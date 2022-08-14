package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SurveyService {
    Long create(SurveyDto.Create dto);
    Optional<SurveyDto.Detail> findById(Long id);
    Page<SurveyDto.Lizt> find(Pageable pageable);
}
