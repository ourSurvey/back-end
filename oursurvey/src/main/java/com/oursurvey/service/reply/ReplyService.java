package com.oursurvey.service.reply;

import com.oursurvey.dto.repo.ReplyDto;

import java.util.List;

public interface ReplyService {
    Long create(ReplyDto.Create dto);
    List<ReplyDto.MyList> findByUserId(Long userId);
    List<Long> findIdBySurveyId(String id);
}
