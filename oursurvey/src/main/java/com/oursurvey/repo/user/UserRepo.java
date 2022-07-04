package com.oursurvey.repo.user;

import com.oursurvey.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long>, UserRepoCustom {
}
