package com.oursurvey.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilterException {
    JWT_ERROR("JWT_ERROR", "Invalid Token"),;

    private String errorName;
    private String message;
}
