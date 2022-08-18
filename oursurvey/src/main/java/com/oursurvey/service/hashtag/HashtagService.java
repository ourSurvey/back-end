package com.oursurvey.service.hashtag;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.entity.Hashtag;

import java.util.List;

public interface HashtagService {
    List<String> findListByValue(String value);
}
