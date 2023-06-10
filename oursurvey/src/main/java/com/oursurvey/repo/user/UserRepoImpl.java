package com.oursurvey.repo.user;

import com.oursurvey.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.oursurvey.entity.QGrade.grade;
import static com.oursurvey.entity.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class UserRepoImpl implements UserRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<User> getBaseJoin() {
        return factory.selectFrom(user).join(user.grade, grade).fetchJoin();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.ofNullable(getBaseJoin().where(user.email.eq(email)).fetchOne());
    }
}
