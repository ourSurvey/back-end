package com.oursurvey.repo.grade;

import com.oursurvey.entity.Grade;

import java.util.List;

public interface GradeRepoCustom {
    Grade getFirstGrade();
    List<Grade> getDesc();
}
