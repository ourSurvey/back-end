package com.oursurvey.repo.survey;

import com.oursurvey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepo extends JpaRepository<Survey, Long>, SurveyRepoCustom {
}
