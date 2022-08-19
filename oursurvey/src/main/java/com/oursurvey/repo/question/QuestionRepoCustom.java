package com.oursurvey.repo.question;

import com.oursurvey.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepoCustom {
    List<Question> getBySectionId(Long id);

    Optional<Question> getFromId(Long id);
}
