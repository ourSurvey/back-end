package com.oursurvey.repo.pkmanager;

import com.oursurvey.entity.PkManager;

import java.util.Optional;

public interface PkManagerRepoCustom {
    Optional<PkManager> getByTableName(String name);
}
