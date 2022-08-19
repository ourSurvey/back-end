package com.oursurvey.repo.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AnswerRepoImpl implements AnswerRepoCustom {
    private final JPAQueryFactory factory;

}
