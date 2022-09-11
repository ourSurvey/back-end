package com.oursurvey.repo.reply;

import com.oursurvey.entity.Reply;

import java.util.Optional;

public interface ReplyRepoCustom {
    Optional<Reply> getBySurveyIdUserId(String surveyId, Long userId);
}
