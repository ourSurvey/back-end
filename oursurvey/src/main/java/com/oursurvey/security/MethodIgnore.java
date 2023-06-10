package com.oursurvey.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@AllArgsConstructor
public enum MethodIgnore {
    GET_IGNORE(List.of()),
    POST_IGNORE(List.of("/api/auth/login")),
    PUT_IGNORE(List.of()),
    PATCH_IGNORE(List.of()),
    DELETE_IGNORE(List.of()),;

    private final List<String> ignores;

    public String[] toStringArray() {
        return StringUtils.toStringArray(this.ignores);
    }
}
