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
        private String pwd;
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
}
