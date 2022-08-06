package com.oursurvey.service.loggedin;

import com.oursurvey.dto.repo.LoggedInDto;
import com.oursurvey.entity.LoggedIn;
import com.oursurvey.entity.User;
import com.oursurvey.repo.loggedin.LoggedInRepo;
import com.oursurvey.repo.loggedin.LoggedInRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoggedInServiceImpl implements LoggedInService {
    private final LoggedInRepo repo;

    @Override
    public Optional<LoggedInDto.Base> findByUserIdDate(Long userId, String date) {
        Optional<LoggedIn> opt = repo.getByUserIdDate(userId, date);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(entityToDto(opt.get()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(LoggedInDto.Create dto) {
        return repo.save(LoggedIn.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .remoteAddr(dto.getRemoteAddr())
                .build()).getId();
    }
}
