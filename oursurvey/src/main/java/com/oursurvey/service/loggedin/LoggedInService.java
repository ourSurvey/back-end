package com.oursurvey.service.loggedin;

import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.entity.LoggedIn;
import com.oursurvey.entity.User;
import com.oursurvey.repo.loggedin.LoggedInRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoggedInService {
    private final LoggedInRepo repo;

    public Optional<LoggedInDto.Base> findByUserIdDate(Long userId, LocalDate date) {
        Optional<LoggedIn> opt = repo.getByUserIdDate(userId, date);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(entityToDto(opt.get()));
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(LoggedInDto.Create dto) {
        return repo.save(LoggedIn.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .remoteAddr(dto.getRemoteAddr())
                .build()).getId();
    }

    private LoggedInDto.Base entityToDto(LoggedIn ent) {
        return LoggedInDto.Base.builder()
                .id(ent.getId())
                .userId(ent.getUser().getId())
                .remoteAddr(ent.getRemoteAddr())
                .createdDt(ent.getCreateDt())
                .build();
    }
}
