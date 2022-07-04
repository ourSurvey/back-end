package com.oursurvey.repo.user;

import com.oursurvey.dto.repo.user.UserDto;
import com.oursurvey.entity.User;

import java.util.Optional;

public interface UserRepoCustom {
    Optional<User> findByEmail(String email);
}
