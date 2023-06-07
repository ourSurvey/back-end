package com.oursurvey.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Login {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String pwd;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Join {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String nickname;

        @NotBlank
        private String pwd;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Addition {
        private String gender;
        private Integer age;
        private String tel;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Certified {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String code;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Resetpwd {
        @NotBlank
        private String token;

        @NotBlank
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
}
