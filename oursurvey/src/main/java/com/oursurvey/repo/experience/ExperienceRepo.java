package com.oursurvey.repo.experience;

import com.oursurvey.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepo extends JpaRepository<Experience, Long>, ExperienceRepoCustom {
}
