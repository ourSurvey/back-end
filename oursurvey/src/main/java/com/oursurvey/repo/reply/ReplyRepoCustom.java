package com.oursurvey.repo.reply;

import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.entity.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepoCustom {
    Optional<Reply> getBySurveyIdUserId(String surveyId, Long userId);
    List<ReplyDto.MyList> getByUserId(Long userId);
    List<Long> getIdBySurveyId(String id);
}
