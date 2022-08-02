package com.oursurvey.service.questionitem;

import com.oursurvey.dto.repo.QuestionItemDto;

public interface QuestionItemService {
    Long create(QuestionItemDto.Create dto);
}
