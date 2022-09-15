package com.oursurvey.repo.answer;

import com.oursurvey.entity.Answer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QAnswer.answer;

@Slf4j
@RequiredArgsConstructor
public class AnswerRepoImpl implements AnswerRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public List<Answer> getByQuestionIds(List<String> questionIds) {
        return factory.selectFrom(answer).where(answer.question.id.in(questionIds)).fetch();
    }

    @Override
    public List<Answer> getByReplyId(Long id) {
        return factory.selectFrom(answer).where(answer.reply.id.eq(id)).fetch();
    }
}
