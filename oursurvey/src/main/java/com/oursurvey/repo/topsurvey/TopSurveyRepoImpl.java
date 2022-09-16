package com.oursurvey.repo.topsurvey;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TopSurveyRepoImpl implements TopSurveyRepoCustom {
    private final JPAQueryFactory factory;
}
