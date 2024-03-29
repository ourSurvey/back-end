package com.oursurvey.service.experience;

import com.oursurvey.dto.repo.ExperienceDto;
import com.oursurvey.entity.Experience;
import com.oursurvey.entity.Grade;
import com.oursurvey.entity.User;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.repo.experience.ExperienceRepo;
import com.oursurvey.repo.grade.GradeRepo;
import com.oursurvey.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepo repo;
    private final UserRepo userRepo;
    private final GradeRepo gradeRepo;

    public Long create(ExperienceDto.Create dto) {
        return repo.save(Experience.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .value(dto.getValue())
                .reason(dto.getReason())
                .tablePk(dto.getTablePk().toString())
                .tableName(dto.getTableName())
                .build()).getId();
    }

    @Transactional
    public void findSumAndPromoting() {
        List<ExperienceDto.ValueOfUser> list = repo.getSumByUserId();
        List<Grade> gradeList = gradeRepo.getDesc();

        list.forEach(e -> {
            User user = userRepo.findById(e.getUserId()).orElseThrow(() -> {
                throw new ObjectNotFoundException("Not Found User");
            });

            Grade userGrade = user.getGrade();
            Long gradeId = userGrade.getId();
            Integer gradePivot = userGrade.getPivot();
            Integer sumExp = e.getSumValue();

            for (Grade g : gradeList) {
                if (g.getPivot() < sumExp && !g.getId().equals(gradeId) && g.getPivot() > gradePivot) {
                    user.promoteGrade(Grade.builder().id(g.getId()).build());
                    break;
                }
            }
        });
    }
}
