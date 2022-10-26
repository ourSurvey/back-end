package com.oursurvey.repo.survey;

import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.entity.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.oursurvey.entity.QHashtag.hashtag;
import static com.oursurvey.entity.QHashtagSurvey.hashtagSurvey;
import static com.oursurvey.entity.QReply.reply;
import static com.oursurvey.entity.QSurvey.survey;
import static com.oursurvey.entity.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class SurveyRepoImpl implements SurveyRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<Survey> getBaseJoin() {
        return factory.selectFrom(survey).join(survey.user, user).fetchJoin();
    }

    @Override
    public Optional<Survey> getFromId(String id) {
        return Optional.ofNullable(getBaseJoin().where(survey.id.eq(id)).fetchOne());
    }

    @Override
    public Page<SurveyDto.Lizt> get(Pageable pageable, String searchText) {
        JPAQuery<SurveyDto.Lizt> query = factory.select(
                        Projections.constructor(
                                SurveyDto.Lizt.class,
                                survey.id,
                                survey.subject,
                                survey.content,
                                survey.openFl,
                                survey.minute,
                                survey.startDate,
                                survey.endDate,
                                survey.createDt,
                                ExpressionUtils.template(String.class, "GROUP_CONCAT({0})", hashtag.value)
                        )
                ).from(survey).leftJoin(hashtagSurvey).on(survey.id.eq(hashtagSurvey.survey.id))
                .leftJoin(hashtag).on(hashtagSurvey.hashtag.id.eq(hashtag.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(survey.id)
                .orderBy(survey.pullDate.desc(), survey.createDt.desc());

        JPAQuery<String> countQuery = factory.select(survey.id).from(survey).leftJoin(hashtagSurvey).on(survey.id.eq(hashtagSurvey.survey.id))
                .leftJoin(hashtag).on(hashtagSurvey.hashtag.id.eq(hashtag.id))
                .groupBy(survey.id);

        if (StringUtils.hasText(searchText)) {
            query.where(survey.subject.contains(searchText).or(hashtag.value.contains(searchText)));
        }

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetch().size());
    }

    @Override
    public List<SurveyDto.MyList> getByUserId(Long userId) {
        return factory.select(
                        Projections.constructor(
                                SurveyDto.MyList.class,
                                survey.id,
                                survey.subject,
                                survey.startDate,
                                survey.endDate,
                                survey.tempFl,
                                reply.count()
                        )
                ).from(survey).leftJoin(reply).on(survey.id.eq(reply.survey.id))
                .where(survey.user.id.eq(userId).and(survey.tempFl.eq(0)))
                .groupBy(survey.id)
                .orderBy(survey.startDate.desc())
                .fetch();
    }

    @Override
    public List<Survey> getTempByUserId(Long userId) {
        return factory.selectFrom(survey).where(survey.user.id.eq(userId).and(survey.tempFl.eq(1))).orderBy(survey.createDt.desc()).fetch();
    }
}
