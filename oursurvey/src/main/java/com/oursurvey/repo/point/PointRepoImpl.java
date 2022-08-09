package com.oursurvey.repo.point;

import com.oursurvey.entity.QPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.oursurvey.entity.QPoint.point;

@Slf4j
@RequiredArgsConstructor
public class PointRepoImpl implements PointRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Integer getSumByUserId(Long id) {
        return factory.select(point.value.sum()).from(point)
                .where(point.user.id.eq(id))
                .groupBy(point.user.id)
                .fetchOne();
    }
}
