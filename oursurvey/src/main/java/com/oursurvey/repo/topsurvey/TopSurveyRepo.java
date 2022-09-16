package com.oursurvey.repo.topsurvey;

import com.oursurvey.entity.TopSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopSurveyRepo extends JpaRepository<TopSurvey, Long>, TopSurveyRepoCustom {
}
