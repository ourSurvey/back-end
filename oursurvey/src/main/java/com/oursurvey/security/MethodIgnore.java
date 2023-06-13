package com.oursurvey.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@AllArgsConstructor
public enum MethodIgnore {
    GET_IGNORE(List.of(
            "/",
            "/api/auth/logout",
            "/api/auth/refresh",
            "/api/auth/validate",
            "/api/active"
    )),

    POST_IGNORE(List.of(
            "/api/auth/login",
            "/api/auth/join",
            "/api/auth/take",
            "/api/auth/certified",
            "/api/auth/findpwd",
            "/api/auth/resetpwd",
            "/api/reply"
    )),

    PUT_IGNORE(List.of()),

    PATCH_IGNORE(List.of()),

    DELETE_IGNORE(List.of()),;

    private final List<String> ignores;

    public String[] toStringArray() {
        return StringUtils.toStringArray(this.ignores);
    }
}
