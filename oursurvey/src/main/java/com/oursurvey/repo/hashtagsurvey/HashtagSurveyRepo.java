package com.oursurvey.repo.hashtagsurvey;

import com.oursurvey.entity.HashtagSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagSurveyRepo extends JpaRepository<HashtagSurvey, Long>, HashtagSurveyRepoCustom {
}
