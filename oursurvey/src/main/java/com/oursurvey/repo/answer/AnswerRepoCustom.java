package com.oursurvey.repo.answer;

import com.oursurvey.entity.Answer;

import java.util.List;

public interface AnswerRepoCustom {
    List<Answer> getByReplyId(Long id);
}
