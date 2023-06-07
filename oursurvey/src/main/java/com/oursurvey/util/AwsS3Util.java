package com.oursurvey.util;

import com.oursurvey.exception.S3FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @Slf4j
// @Component
// @RequiredArgsConstructor
public class AwsS3Util {
    // private final AmazonS3Client s3Client;
    //
    // @Value("${cloud.aws.s3.bucket}")
    // private String BUCKET;
    //
    // private final List<String> CAN_EXTS = new ArrayList<>(Arrays.asList("jpeg", "jpg", "gif", "png"));
    // private final Long CAN_SIZE = 3000000L; // 3MB
    //
    // public PutObjectResult put(MultipartFile file, String key) {
    //     try (InputStream inputStream = file.getInputStream()) {
    //         ObjectMetadata objectMetadata = new ObjectMetadata();
    //         objectMetadata.setContentType(file.getContentType());
    //         return s3Client.putObject(new PutObjectRequest(
    //                 BUCKET,
    //                 key,
    //                 inputStream,
    //                 objectMetadata
    //         ));
    //     } catch (IOException e) {
    //         throw new S3FileUploadException();
    //     }
    // }
    //
    // public void delete(String key) {
    //     s3Client.deleteObject(new DeleteObjectRequest(BUCKET, key));
    // }
    //
    // public boolean checkFile(MultipartFile file) {
    //     if (file == null || file.getSize() == 0 || file.getSize() > CAN_SIZE) {
    //         return false;
    //     }
    //
    //     String originName = file.getOriginalFilename();
    //     String ext = originName.substring(originName.lastIndexOf(".") + 1).toLowerCase();
    //     if (!CAN_EXTS.contains(ext)) {
    //         return false;
    //     }
    //
    //     return true;
    // }
    //
    // public String getPrefixPath() {
    //     return "https://oursurvey-s3.s3.ap-northeast-2.amazonaws.com/";
    // }
}
