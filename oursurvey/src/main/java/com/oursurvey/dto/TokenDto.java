package com.oursurvey.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Integer refreshTokenExpire;


    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String tokenType;
        private String access;
        private String refresh;
    }
}