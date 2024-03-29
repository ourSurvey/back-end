package com.oursurvey.service.point;

import com.oursurvey.dto.repo.PointDto;
import com.oursurvey.entity.Point;
import com.oursurvey.entity.User;
import com.oursurvey.repo.point.PointRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PointService {
    private final PointRepo repo;

    @Transactional(rollbackFor = Exception.class)
    public Integer create(PointDto.Create dto) {
        Point save = repo.save(Point.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .value(dto.getValue())
                .reason(dto.getReason())
                .tablePk(String.valueOf(dto.getTablePk()))
                .tableName(dto.getTableName())
                .build());

        // 적립된 포인트 리턴
        return save.getValue();
    }

    public Integer findSumByUserId(Long id) {
        Integer sum = repo.getSumByUserId(id);
        return sum == null ? 0 : sum;
    }
}
