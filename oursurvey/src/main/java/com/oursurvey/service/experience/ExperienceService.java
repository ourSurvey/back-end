package com.oursurvey.service.experience;

import com.oursurvey.dto.repo.ExperienceDto;

public interface ExperienceService {
    Long create(ExperienceDto.Create dto);
    void findSumAndPromoting();
}
