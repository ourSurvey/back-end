package com.oursurvey.repo.section;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SectionRepoImpl implements SectionRepoCustom {
    private final JPAQueryFactory factory;
}
