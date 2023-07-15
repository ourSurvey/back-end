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
public class UserService  {
    private final UserRepo repo;
    private final GradeRepo gradeRepo;
    private final PointRepo pointRepo;

    public UserDto.Basic findByEmail(String email) {
        User user = repo.findByEmail(email).orElseThrow(() -> {
            throw new ObjectNotFoundException("Not Found User");
        });

        return UserDto.Basic.builder().user(user).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(UserDto.Create dto) {
        Grade grade = gradeRepo.getFirstGrade();
        User save = repo.save(User.builder()
                .grade(grade)
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

    @Transactional(rollbackFor = Exception.class)
    public void updateAddition(Long id, UserDto.UpdateAddition dto) {
       repo.findById(id).ifPresent(user ->
               user.updateAddition(dto.getGender(), dto.getAge(), dto.getTel())
       );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePwd(String email, String pwd) {
        repo.findByEmail(email).ifPresent(user ->
                user.updatePwd(pwd)
        );
    }
}
