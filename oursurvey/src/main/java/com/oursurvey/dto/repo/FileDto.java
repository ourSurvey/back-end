package com.oursurvey.dto.repo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class FileDto {

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Base {
        private Long id;
        private Long userId;
        private Long tablePk;
        private String tableName;
        private String originName;
        private String dir;
        private String name;
        private String path;
        private String ext;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private Long userId;
        private String originName;
        private String name;
        private String path;
        private String ext;
        private MultipartFile file;

        public void setPath(String path) {
            this.path = path;
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateResult {
        private Long id;
        private String path;
    }
}
