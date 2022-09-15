package com.oursurvey.service.answer;

import com.oursurvey.dto.repo.AnswerDto;
import com.oursurvey.entity.Answer;

import java.util.List;

public interface AnswerService {
    List<AnswerDto.Base> findByQuestionIds(List<String> questionIds);
    List<AnswerDto.Base> findByReplyId(Long id);

    default AnswerDto.Base entityToDto(Answer entity) {
        return AnswerDto.Base.builder()
                .id(entity.getId())
                .replyId(entity.getReply().getId())
                .questionId(entity.getQuestion().getId())
                .response(entity.getResponse())
                .build();
    }
}
