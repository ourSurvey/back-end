package com.oursurvey.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String pwd;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    public static class Join {
        private String email;
        private String nickname;
        private String pwd;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    public static class Addition {
        private String gender;
        private Integer age;
        private String tel;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    public static class Certified {
        private String email;
        private String code;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Resetpwd {
        private String token;
        private String pwd;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class LoginReponse {
        private String tokenType;
        private String access;
        private String refresh;
        private Integer refreshExpire;
        private Integer sumPoint;
        private Integer savedPoint;

        public void setSavedPoint(Integer point) {
            this.savedPoint = point;
        }
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class JoinResponse {
        private String tokenType;
        private String access;
        private Integer savedPoint;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    public static class FindPasswordResponse {
        private String token;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class EmailDto {
        private String email;
    }
}
