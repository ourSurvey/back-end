package com.oursurvey.repo.hashtagsurvey;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.entity.HashtagSurvey;
import com.oursurvey.entity.QHashtag;
import com.oursurvey.entity.QHashtagSurvey;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QHashtag.hashtag;
import static com.oursurvey.entity.QHashtagSurvey.hashtagSurvey;

@Slf4j
@RequiredArgsConstructor
public class HashtagSurveyRepoImpl implements HashtagSurveyRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public List<HashtagDto.Base> getBySurveyId(String id) {
        return factory.select(
                        Projections.constructor(
                                HashtagDto.Base.class,
                                hashtag.value
                        )
                )
                .from(hashtagSurvey).join(hashtag).on(hashtagSurvey.hashtag.id.eq(hashtag.id))
                .where(hashtagSurvey.survey.id.eq(id))
                .fetch();
    }
}
