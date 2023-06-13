package com.oursurvey.repo.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.entity.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.bytecode.spi.ReflectionOptimizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
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
    public Page<SurveyDto.Lizt> get(Pageable pageable, String condition, Object obj) {
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
                                Expressions.stringTemplate("group_concat({0})", hashtag.value)
                        )
                ).from(survey)
                .leftJoin(hashtagSurvey).on(survey.id.eq(hashtagSurvey.survey.id))
                .leftJoin(hashtag).on(hashtagSurvey.hashtag.id.eq(hashtag.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(survey.id)
                .orderBy(survey.pullDate.desc(), survey.createDt.desc());

        JPAQuery<String> countQuery = factory.select(survey.id)
                .from(survey)
                .leftJoin(hashtagSurvey).on(survey.id.eq(hashtagSurvey.survey.id))
                .leftJoin(hashtag).on(hashtagSurvey.hashtag.id.eq(hashtag.id))
                .groupBy(survey.id);

        if (condition.equals("searchText")) {
            String searchText = String.valueOf(obj);
            query.where(survey.subject.contains(searchText).or(hashtag.value.contains(searchText)));
            countQuery.where(survey.subject.contains(searchText).or(hashtag.value.contains(searchText)));
        }

        if (condition.equals("surveyIds")) {
            List<String> surveyIds = (List<String>) obj;
            query.where(survey.id.in(surveyIds));
            countQuery.where(survey.id.in(surveyIds));
        }

        if (condition.equals("minute")) {
            Integer minute = (Integer) obj;
            query.where(survey.minute.loe(minute));
            countQuery.where(survey.minute.loe(minute));
        }

        if (condition.equals("openFl")) {
            Integer openFl = (Integer) obj;
            query.where(survey.openFl.eq(openFl));
            countQuery.where(survey.openFl.eq(openFl));
        }

        if (condition.equals("viewCnt")) {
            query.orderBy(survey.viewCnt.desc());

            LocalDate now = LocalDate.now();
            query.where(survey.startDate.after(now));
            query.where(survey.endDate.before(now));
            countQuery.where(survey.startDate.after(now));
            countQuery.where(survey.endDate.before(now));
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
