package com.oursurvey.service.loggedin;

import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.entity.LoggedIn;

import java.time.LocalDate;
import java.util.Optional;

public interface LoggedInService {
    Optional<LoggedInDto.Base> findByUserIdDate(Long userId, LocalDate date);
    Long create(LoggedInDto.Create dto);

    default LoggedInDto.Base entityToDto(LoggedIn ent) {
        return LoggedInDto.Base.builder()
                .id(ent.getId())
                .userId(ent.getUser().getId())
                .remoteAddr(ent.getRemoteAddr())
                .createdDt(ent.getCreateDt())
                .build();
    }
}
