package com.oursurvey.repo.hashtag;

import com.oursurvey.entity.Hashtag;
import com.oursurvey.entity.QHashtag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.oursurvey.entity.QHashtag.hashtag;

@Slf4j
@RequiredArgsConstructor
public class HashtagRepoImpl implements HashtagRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Optional<Hashtag> getByValue(String value) {
        return Optional.ofNullable(factory.selectFrom(hashtag).where(hashtag.value.eq(value)).fetchOne());
    }

    @Override
    public List<Hashtag> getListByValue(String value) {
        return factory.selectFrom(hashtag).where(hashtag.value.contains(value)).fetch();
    }
}
