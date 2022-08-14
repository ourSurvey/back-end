package com.oursurvey.repo.questionitem;

import com.oursurvey.entity.QQuestion;
import com.oursurvey.entity.QQuestionItem;
import com.oursurvey.entity.QuestionItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QQuestion.question;
import static com.oursurvey.entity.QQuestionItem.questionItem;

@Slf4j
@RequiredArgsConstructor
public class QuestionItemRepoImpl implements QuestionItemRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<QuestionItem> getBaseJoin() {
        return factory.selectFrom(questionItem).join(questionItem.question, question).fetchJoin();
    }

    @Override
    public List<QuestionItem> getByQuestionId(Long id) {
        return getBaseJoin().where(questionItem.question.id.eq(id)).fetch();
    }
}
