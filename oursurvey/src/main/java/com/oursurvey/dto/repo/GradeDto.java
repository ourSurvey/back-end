package com.oursurvey.dto.repo;

import com.oursurvey.entity.Grade;
import lombok.*;

public class GradeDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Basic {
        private Long id;
        private Integer pivot;
        private String name;

        @Builder
        public Basic(Grade entity) {
            this.id = entity.getId();
            this.pivot = entity.getPivot();
            this.name = entity.getName();
        }
    }
}
