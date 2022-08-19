package com.oursurvey.repo.question;

import com.oursurvey.entity.QQuestion;
import com.oursurvey.entity.QSection;
import com.oursurvey.entity.Question;
import com.oursurvey.entity.Section;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.oursurvey.entity.QQuestion.question;
import static com.oursurvey.entity.QSection.section;

@Slf4j
@RequiredArgsConstructor
public class QuestionRepoImpl implements QuestionRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<Question> getBaseJoin() {
        return factory.selectFrom(question).join(question.section, section).fetchJoin();
    }

    @Override
    public List<Question> getBySectionId(Long id) {
        return getBaseJoin().where(question.section.id.eq(id)).fetch();
    }

    @Override
    public Optional<Question> getFromId(Long id) {
        return Optional.ofNullable(getBaseJoin().where(question.id.eq(id)).fetchOne());
    }
}
