package com.oursurvey.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("access", 60 * 60 * 24),
    REFRESH_TOKEN("refresh", 60 * 60 * 24 * 7),
    TOKEN_EXPIRE_5MINUTE("temp", 60 * 5),;

    private String type;
    private Integer second;
}
