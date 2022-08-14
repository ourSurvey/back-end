package com.oursurvey.repo.hashtagsurvey;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.entity.HashtagSurvey;

import java.util.List;

public interface HashtagSurveyRepoCustom {
    List<HashtagDto.Base> getBySurveyId(Long id);
}
