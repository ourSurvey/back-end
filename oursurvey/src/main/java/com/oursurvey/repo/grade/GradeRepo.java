package com.oursurvey.repo.grade;

import com.oursurvey.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepo extends JpaRepository<Grade, Long>, GradeRepoCustom {

}
