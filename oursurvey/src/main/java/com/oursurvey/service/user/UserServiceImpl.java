package com.oursurvey.service.user;

import com.oursurvey.dto.repo.UserDto;
import com.oursurvey.entity.Grade;
import com.oursurvey.entity.User;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.repo.grade.GradeRepo;
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

    @Override
    public Optional<UserDto.Basic> findByEmail(String email) {
        Optional<User> opt = repo.getByEmail(email);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(UserDto.Basic.builder().entity(opt.get()).build());
    }

    @Override
    public Long create(UserDto.Create dto) {
        Grade firstGrade = gradeRepo.getFirstGrade();
        User save = repo.save(User.builder().grade(Grade.builder().id(firstGrade.getId()).build())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .pwd(dto.getPwd())
                .build());

        return save.getId();
    }

    @Override
    public void updateAddition(Long id, UserDto.UpdateAddition dto) {
        User user = repo.getReferenceById(id);
        if (user == null) {
            throw new ObjectNotFoundException();
        }

        user.updateAddition(dto.getGender(), dto.getAge(), dto.getTel());
    }

    @Override
    public void updatePwd(String email, String pwd) throws Exception {
        Optional<User> opt = repo.getByEmail(email);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException();
        }

        User user = opt.get();
        user.updatePwd(pwd);
    }
}
