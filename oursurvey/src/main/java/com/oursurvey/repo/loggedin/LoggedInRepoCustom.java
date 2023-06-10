package com.oursurvey.repo.loggedin;

import com.oursurvey.entity.LoggedIn;

import java.time.LocalDate;
import java.util.Optional;

public interface LoggedInRepoCustom {
    Optional<LoggedIn> getByUserIdDate(Long userId, LocalDate date);
}
