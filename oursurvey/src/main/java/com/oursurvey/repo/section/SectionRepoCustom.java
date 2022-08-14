package com.oursurvey.repo.section;

import com.oursurvey.entity.Section;

import java.util.List;

public interface SectionRepoCustom {
    List<Section> getBySurveyId(Long id);
}
