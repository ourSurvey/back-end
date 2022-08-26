package com.oursurvey.service;

import com.oursurvey.dto.repo.ExperienceDto;
import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.dto.repo.PointDto;

public interface TransactionService {
    void saveLogAndPointAndExperience(LoggedInDto.Create logDto, PointDto.Create pointDto, ExperienceDto.Create experienceDto);
}
