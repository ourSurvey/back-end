package com.oursurvey.service.user;

import com.oursurvey.dto.repo.UserDto;
import com.oursurvey.entity.Grade;
import com.oursurvey.entity.Point;
import com.oursurvey.entity.User;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.repo.grade.GradeRepo;
import com.oursurvey.repo.point.PointRepo;
import com.oursurvey.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final GradeRepo gradeRepo;
    private final PointRepo pointRepo;

    @Override
    public Optional<UserDto.Basic> findByEmail(String email) {
        Optional<User> opt = repo.getByEmail(email);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(UserDto.Basic.builder().entity(opt.get()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(UserDto.Create dto) {
        Grade firstGrade = gradeRepo.getFirstGrade();
        User save = repo.save(User.builder().grade(Grade.builder().id(firstGrade.getId()).build())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(dto.getPwd())
                .build());

        // save point
        pointRepo.save(Point.builder()
                .user(save)
                .value(Point.JOIN_VALUE)
                .reason(Point.JOIN_REASON)
                .tablePk(String.valueOf(save.getId()))
                .tableName("user")
                .build());

        return save.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddition(Long id, UserDto.UpdateAddition dto) {
        User user = repo.getReferenceById(id);
        if (user == null) {
            throw new ObjectNotFoundException();
        }

        user.updateAddition(dto.getGender(), dto.getAge(), dto.getTel());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePwd(String email, String pwd) throws Exception {
        Optional<User> opt = repo.getByEmail(email);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException();
        }

        User user = opt.get();
        user.updatePwd(pwd);
    }
}
