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
public class AnswerService {
    private final AnswerRepo repo;

    public List<AnswerDto.Base> findByQuestionIds(List<String> questionIds) {
        return repo.getByQuestionIds(questionIds).stream().map(this::entityToDto).toList();
    }

    public List<AnswerDto.Base> findByReplyId(Long id) {
        return repo.getByReplyId(id).stream().map(this::entityToDto).toList();
    }

    private AnswerDto.Base entityToDto(Answer entity) {
        return AnswerDto.Base.builder()
                .id(entity.getId())
                .replyId(entity.getReply().getId())
                .questionId(entity.getQuestion().getId())
                .response(entity.getResponse())
                .build();
    }
}
