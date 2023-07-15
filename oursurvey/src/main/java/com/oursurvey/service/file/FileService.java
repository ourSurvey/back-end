package com.oursurvey.service.file;

import com.oursurvey.dto.repo.FileDto;
import com.oursurvey.entity.File;
import com.oursurvey.repo.file.FileRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    private final FileRepo repo;

    @Transactional
    public FileDto.CreateResult create(FileDto.Create dto) {
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + "/";
        String key = dir + dto.getName();
        // awsS3Util.put(dto.getFile(), key);

        // String path = awsS3Util.getPrefixPath() + key;
        // dto.setPath(path);
        // File save = repo.save(File.builder()
        //         .user(User.builder().id(dto.getUserId()).build())
        //         .originName(dto.getOriginName())
        //         .dir(dir)
        //         .name(dto.getName())
        //         .path(path)
        //         .ext(dto.getExt())
        //         .build());
        //
        // return FileDto.CreateResult.builder()
        //         .id(save.getId())
        //         .path(path)
        //         .build();

        return null;
    }

    public Optional<FileDto.Base> findById(Long id) {
        Optional<File> fileOpt = repo.getFromId(id);
        if (fileOpt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(entityToDto(fileOpt.get()));
    }

    public List<FileDto.Base> findByPkName(String pk, String name) {
        return null;
    }

    @Transactional
    public void delete(Long id) {
        File file = repo.getFromId(id).get();

        repo.deleteById(id);
        // awsS3Util.delete(file.getDir() + file.getName());
    }

    private FileDto.Base entityToDto(File file) {
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
