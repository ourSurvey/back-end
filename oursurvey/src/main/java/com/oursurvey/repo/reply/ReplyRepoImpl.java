package com.oursurvey.repo.reply;

import com.oursurvey.entity.QReply;
import com.oursurvey.entity.Reply;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
