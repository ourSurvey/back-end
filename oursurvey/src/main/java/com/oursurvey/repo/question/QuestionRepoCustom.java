package com.oursurvey.repo.question;

import com.oursurvey.entity.Question;

import java.util.List;

public interface QuestionRepoCustom {
    List<Question> getBySectionId(Long id);
}
