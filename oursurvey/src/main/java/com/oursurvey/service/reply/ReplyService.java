package com.oursurvey.service.reply;

import com.oursurvey.dto.repo.ReplyDto;

public interface ReplyService {
    Long create(ReplyDto.Create dto);
}
