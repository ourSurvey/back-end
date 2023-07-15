package com.oursurvey.service.questionitem;

import com.oursurvey.dto.repo.QuestionItemDto;
import com.oursurvey.repo.questionitem.QuestionItemRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class QuestionItemService {
    private final QuestionItemRepo repo;

    @Transactional(rollbackFor = Exception.class)
    public Long create(QuestionItemDto.Create dto) {
        return null;
    }
}
