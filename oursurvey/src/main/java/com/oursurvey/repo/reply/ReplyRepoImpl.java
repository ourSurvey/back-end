package com.oursurvey.repo.reply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ReplyRepoImpl implements ReplyRepoCustom {
    private final JPAQueryFactory factory;
}
