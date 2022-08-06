package com.oursurvey.repo.loggedin;

import com.oursurvey.entity.LoggedIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggedInRepo extends JpaRepository<LoggedIn, Long>, LoggedInRepoCustom {
}
