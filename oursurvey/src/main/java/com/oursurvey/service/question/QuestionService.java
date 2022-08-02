package com.oursurvey.service.question;

import com.oursurvey.dto.repo.QuestionDto;

public interface QuestionService {
    Long create(QuestionDto.Create dto);
}
