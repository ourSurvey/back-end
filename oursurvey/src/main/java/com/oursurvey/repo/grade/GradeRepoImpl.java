package com.oursurvey.repo.grade;

import com.oursurvey.entity.Grade;
import com.oursurvey.entity.QGrade;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QGrade.grade;

@Slf4j
@RequiredArgsConstructor
public class GradeRepoImpl implements GradeRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Grade getFirstGrade() {
        return factory.selectFrom(grade).orderBy(grade.pivot.asc()).limit(1).fetchOne();
    }

    @Override
    public List<Grade> getDesc() {
        return factory.selectFrom(grade).orderBy(grade.pivot.desc()).fetch();
    }
}
