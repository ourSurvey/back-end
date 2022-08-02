package com.oursurvey.repo.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuestionRepoImpl implements QuestionRepoCustom {
    private final JPAQueryFactory factory;
}
