package com.oursurvey.repo.section;

import com.oursurvey.entity.QSection;
import com.oursurvey.entity.QSurvey;
import com.oursurvey.entity.Section;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.oursurvey.entity.QSection.section;
import static com.oursurvey.entity.QSurvey.survey;

@Slf4j
@RequiredArgsConstructor
public class SectionRepoImpl implements SectionRepoCustom {
    private final JPAQueryFactory factory;

    private JPAQuery<Section> getBaseJoin() {
        return factory.selectFrom(section).join(section.survey, survey).fetchJoin();
    }

    @Override
    public List<Section> getBySurveyId(String id) {
        return getBaseJoin().where(section.survey.id.eq(id)).fetch();
    }
}
