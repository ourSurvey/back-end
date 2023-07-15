package com.oursurvey.dto.repo;

import com.oursurvey.dto.repo.GradeDto;
import com.oursurvey.entity.Enums;
import com.oursurvey.entity.User;
import lombok.*;

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
        private Integer age;
        private String tel;

        @Builder
        public Basic(User user) {
            this.id = user.getId();
            this.grade = GradeDto.Basic.builder().entity(user.getGrade()).build();
            this.email = user.getEmail();
            this.pwd = user.getPwd();
            this.gender = user.getGender();
            this.age = user.getAge();
            this.tel = user.getTel();
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

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateAddition {
        private String gender;
        private Integer age;
        private String tel;
    }
}
