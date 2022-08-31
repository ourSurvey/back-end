package com.oursurvey.exception;

public class S3FileUploadException extends RuntimeException {
    public S3FileUploadException() {
    }

    public S3FileUploadException(String message) {
        super(message);
    }

    public S3FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3FileUploadException(Throwable cause) {
        super(cause);
    }

    public S3FileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
