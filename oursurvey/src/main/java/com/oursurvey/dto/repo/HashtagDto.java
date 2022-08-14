package com.oursurvey.dto.repo;

import lombok.*;

public class HashtagDto {

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Base {
        private String value;
    }
}
