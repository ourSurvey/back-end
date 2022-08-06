package com.oursurvey.repo.loggedin;

import com.oursurvey.entity.LoggedIn;

import java.util.Optional;

public interface LoggedInRepoCustom {
    Optional<LoggedIn> getByUserIdDate(Long userId, String date);
}
