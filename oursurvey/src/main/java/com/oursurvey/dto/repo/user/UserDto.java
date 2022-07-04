package com.oursurvey.dto.repo.user;

import com.oursurvey.dto.repo.grade.GradeDto;
import com.oursurvey.entity.Enums;
import com.oursurvey.entity.User;
import lombok.*;

import java.time.LocalDate;

public class UserDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Basic {
        private Long id;
        private GradeDto.Basic grade;
        private String email;
        private String nickname;
        private String pwd;
        private Enums gender;
        private LocalDate age;
        private String tel;

        @Builder
        public Basic(User entity) {
            this.id = entity.getId();
            this.grade = GradeDto.Basic.builder().entity(entity.getGrade()).build();
            this.email = entity.getEmail();
            // this.email = entity.get();
            this.pwd = entity.getPwd();
            this.gender = entity.getGender();
            this.age = entity.getAge();
            this.tel = entity.getTel();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String gradeId;
        private String email;
        private String nickname;
        private String pwd;
    }
}
