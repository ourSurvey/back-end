package com.oursurvey.service.section;

import com.oursurvey.dto.repo.SectionDto;

public interface SectionService {
    Long create(SectionDto.Create dto);
}
