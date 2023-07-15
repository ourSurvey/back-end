package com.oursurvey.service.question;

import com.oursurvey.dto.repo.QuestionDto;
import com.oursurvey.repo.question.QuestionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepo repo;

    @Transactional(rollbackFor = Exception.class)
    public Long create(QuestionDto.Create dto) {
        return null;
    }
}
