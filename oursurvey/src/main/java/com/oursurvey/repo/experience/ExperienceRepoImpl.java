package com.oursurvey.repo.experience;

import com.oursurvey.dto.repo.ExperienceDto;
import com.oursurvey.entity.QExperience;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QExperience.experience;
import static com.oursurvey.entity.QPoint.point;

@Slf4j
@RequiredArgsConstructor
public class ExperienceRepoImpl implements ExperienceRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public List<ExperienceDto.ValueOfUser> getSumByUserId() {
        return factory.select(
                        Projections.constructor(
                                ExperienceDto.ValueOfUser.class,
                                experience.user.id,
                                experience.value.sum()
                        )
                )
                .from(experience)
                .groupBy(experience.user.id)
                .fetch();
    }
}
