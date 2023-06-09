package com.oursurvey.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("access", 24 * 7),
    REFRESH_TOKEN("access", 24 * 7 * 30),;

    private String type;
    private Integer hours;
}
