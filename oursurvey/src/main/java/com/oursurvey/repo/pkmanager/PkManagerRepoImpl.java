package com.oursurvey.repo.pkmanager;

import com.oursurvey.entity.PkManager;
import com.oursurvey.entity.QPkManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.oursurvey.entity.QPkManager.pkManager;

@Slf4j
@RequiredArgsConstructor
public class PkManagerRepoImpl implements PkManagerRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Optional<PkManager> getByTableName(String name) {
        return Optional.ofNullable(factory.selectFrom(pkManager).where(pkManager.tableName.eq(name)).fetchOne());
    }
}
