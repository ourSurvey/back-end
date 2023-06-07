package com.oursurvey.controller;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.dto.repo.FileDto;
import com.oursurvey.exception.AuthFailException;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.exception.S3FileUploadException;
import com.oursurvey.service.file.FileService;
import com.oursurvey.util.AwsS3Util;
import com.oursurvey.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public MyResponse post(HttpServletRequest request, MultipartFile file) {
        // if (!awsS3Util.checkFile(file)) {
        //     throw new S3FileUploadException("invalid file");
        // }

        Long userId = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));
        String oName = file.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf(".") + 1).toLowerCase();

        FileDto.CreateResult fileResult = fileService.create(FileDto.Create.builder()
                .userId(userId)
                .originName(oName)
                .name(UUID.randomUUID() + "." + ext)
                .ext(ext)
                .file(file)
                .build());

        return new MyResponse().setData(fileResult);
    }

    @DeleteMapping("/{id}")
    public MyResponse delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = jwtUtil.getLoginUserId(request.getHeader(HttpHeaders.AUTHORIZATION));

        FileDto.Base fileDto = fileService.findById(id).orElseThrow(() -> {
            throw new ObjectNotFoundException("no file");
        });

        if (!fileDto.getUserId().equals(userId)) {
            throw new AuthFailException("its not your file");
        }

        fileService.delete(id);
        return new MyResponse();
    }
}
