package com.oursurvey.repo.survey;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SurveyRepoImpl implements SurveyRepoCustom {
    private final JPAQueryFactory factory;
}
