package com.oursurvey.repo.point;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PointRepoImpl implements PointRepoCustom {
    private final JPAQueryFactory factory;
}
