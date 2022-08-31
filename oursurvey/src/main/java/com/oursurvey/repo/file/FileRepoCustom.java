package com.oursurvey.repo.file;

import com.oursurvey.entity.File;

import java.util.Optional;

public interface FileRepoCustom {
    Optional<File> getFromId(Long id);
}
