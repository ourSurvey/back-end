package com.oursurvey.service;

import com.oursurvey.dto.repo.ExperienceDto;
import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.dto.repo.PointDto;
import com.oursurvey.entity.Experience;
import com.oursurvey.entity.LoggedIn;
import com.oursurvey.entity.Point;
import com.oursurvey.entity.User;
import com.oursurvey.repo.experience.ExperienceRepo;
import com.oursurvey.repo.loggedin.LoggedInRepo;
import com.oursurvey.repo.point.PointRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final PointRepo pointRepo;
    private final ExperienceRepo experienceRepo;
    private final LoggedInRepo logRepo;

    @Override
    public void saveLogAndPointAndExperience(LoggedInDto.Create logDto, PointDto.Create pointDto, ExperienceDto.Create experienceDto) {
        logRepo.save(LoggedIn.builder()
                .user(User.builder().id(logDto.getUserId()).build())
                .remoteAddr(logDto.getRemoteAddr())
                .build());

        pointRepo.save(Point.builder()
                .user(User.builder().id(pointDto.getUserId()).build())
                .value(pointDto.getValue())
                .reason(pointDto.getReason())
                .tablePk(pointDto.getTablePk())
                .tableName(pointDto.getTableName())
                .build());

        experienceRepo.save(Experience.builder()
                .user(User.builder().id(experienceDto.getUserId()).build())
                .value(experienceDto.getValue())
                .reason(experienceDto.getReason())
                .tablePk(experienceDto.getTablePk())
                .tableName(experienceDto.getTableName())
                .build());
    }
}
