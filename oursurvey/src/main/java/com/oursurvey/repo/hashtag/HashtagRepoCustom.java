package com.oursurvey.repo.hashtag;

import com.oursurvey.entity.Hashtag;

import java.util.Optional;

public interface HashtagRepoCustom {
    Optional<Hashtag> getByValue(String value);
}
