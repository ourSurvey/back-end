package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.SurveyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SurveyService {
    String create(SurveyDto.Create dto);
    Optional<SurveyDto.Detail> findById(String id);
    Page<SurveyDto.Lizt> find(Pageable pageable);
    List<SurveyDto.MyList> findByUserId(Long userId);
    List<SurveyDto.MyListTemp> findTempByUserId(Long userId);
    void pull(Long userId, String surveyId);
}
