package com.oursurvey.repo.answer;

import com.oursurvey.entity.Answer;

import java.util.List;

public interface AnswerRepoCustom {
    List<Answer> getByQuestionIds(List<String> questionIds);
    List<Answer> getByReplyId(Long id);
}
