package com.oursurvey.service.answer;

import com.oursurvey.dto.repo.AnswerDto;
import com.oursurvey.entity.Answer;
import com.oursurvey.repo.answer.AnswerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepo repo;

    @Override
    public List<AnswerDto.Base> findByReplyId(Long id) {
        return repo.getByReplyId(id).stream().map(this::entityToDto).toList();
    }
}
