package com.oursurvey.repo.survey;

import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SurveyRepoCustom {
    Optional<Survey> getFromId(String id);
    Page<SurveyDto.Lizt> get(Pageable pageable, String searchText);
    List<SurveyDto.MyList> getByUserId(Long userId);
    List<Survey> getTempByUserId(Long userId);
}
