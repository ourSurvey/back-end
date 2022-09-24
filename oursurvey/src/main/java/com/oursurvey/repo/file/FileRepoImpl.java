package com.oursurvey.repo.file;

import com.oursurvey.entity.File;
import com.oursurvey.entity.QFile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.oursurvey.entity.QFile.file;

@Slf4j
@RequiredArgsConstructor
public class FileRepoImpl implements FileRepoCustom {
    private final JPAQueryFactory factory;

    @Override
    public Optional<File> getFromId(Long id) {
        return Optional.ofNullable(factory.selectFrom(file).where(file.id.eq(id)).fetchOne());
    }

    @Override
    public List<File> getByPkName(String pk, String name) {
        return factory.selectFrom(file).where(file.tablePk.eq(pk).and(file.tableName.eq(name))).fetch();
    }
}
