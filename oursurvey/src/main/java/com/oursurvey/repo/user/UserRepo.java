package com.oursurvey.repo.user;

import com.oursurvey.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, UserRepoCustom {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
