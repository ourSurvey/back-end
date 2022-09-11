package com.oursurvey.repo.pkmanager;

import com.oursurvey.entity.PkManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PkManagerRepo extends JpaRepository<PkManager, Long>, PkManagerRepoCustom {
}
