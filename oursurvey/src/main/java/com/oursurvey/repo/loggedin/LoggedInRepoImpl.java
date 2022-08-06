package com.oursurvey.repo.loggedin;

import com.oursurvey.entity.LoggedIn;
import com.oursurvey.entity.QLoggedIn;
import com.oursurvey.entity.QUser;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLExpressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.oursurvey.entity.QLoggedIn.loggedIn;
import static com.oursurvey.entity.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class LoggedInRepoImpl implements LoggedInRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<LoggedIn> getBaseJoin() {
        return factory.selectFrom(loggedIn).join(loggedIn.user, user).fetchJoin();
    }

    @Override
    public Optional<LoggedIn> getByUserIdDate(Long userId, String date) {
        // FIXME. 더 알아 봐야함
        DateTemplate toDate = Expressions.dateTemplate(
                String.class, "DATE({0})",
                loggedIn.createDt
        );

        return Optional.ofNullable(getBaseJoin().where(loggedIn.user.id.eq(userId).and(toDate.eq(date))).fetchOne());
    }
}
