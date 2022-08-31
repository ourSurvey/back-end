package com.oursurvey.service.file;

import com.oursurvey.dto.repo.FileDto;
import com.oursurvey.entity.File;

import java.util.Optional;

public interface FileService {
    FileDto.CreateResult create(FileDto.Create dto);
    Optional<FileDto.Base> findById(Long id);
    void delete(Long id);

    default FileDto.Base entityToDto(File file) {
        return FileDto.Base.builder()
                .id(file.getId())
                .userId(file.getUser().getId())
                .tablePk(file.getTablePk())
                .tableName(file.getTableName())
                .originName(file.getOriginName())
                .dir(file.getDir())
                .name(file.getName())
                .path(file.getPath())
                .ext(file.getExt())
                .build();
    }
}
