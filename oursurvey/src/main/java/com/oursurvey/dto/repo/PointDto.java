package com.oursurvey.dto.repo;

import lombok.*;

public class PointDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long userId;
        private Integer value;
        private String reason;
        private Long tablePk;
        private String tableName;
    }
}
