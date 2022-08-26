package com.oursurvey.repo.experience;

import com.oursurvey.dto.repo.ExperienceDto;

import java.util.List;

public interface ExperienceRepoCustom {
    List<ExperienceDto.ValueOfUser> getSumByUserId();
}
