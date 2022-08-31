package com.oursurvey.repo.file;

import com.oursurvey.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<File, Long>, FileRepoCustom {

}
