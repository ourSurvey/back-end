package com.oursurvey.repo.reply;

import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.entity.Reply;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.oursurvey.entity.QReply.reply;

@Slf4j
@RequiredArgsConstructor
public class ReplyRepoImpl implements ReplyRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Optional<Reply> getBySurveyIdUserId(String surveyId, Long userId) {
        return Optional.ofNullable(factory.selectFrom(reply)
                .where(reply.survey.id.eq(surveyId))
                .where(userId != null ? (reply.user.id.eq(userId)) : (reply.user.isNull()))
                .fetchOne());
    }

    @Override
    public List<ReplyDto.MyList> getByUserId(Long userId) {
        return factory.select(
                        Projections.constructor(
                                ReplyDto.MyList.class,
                                reply.id
                        )
                ).from(reply).where(reply.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Long> getIdBySurveyId(String id) {
        return factory.select(reply.id).from(reply).where(reply.survey.id.eq(id)).orderBy(reply.createDt.asc()).fetch();
    }
}
