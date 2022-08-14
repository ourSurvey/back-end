package com.oursurvey.repo.questionitem;

import com.oursurvey.entity.QuestionItem;

import java.util.List;

public interface QuestionItemRepoCustom {
    List<QuestionItem> getByQuestionId(Long id);
}
