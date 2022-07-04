package com.oursurvey.exception;

public class CertifiedException extends RuntimeException {
    public CertifiedException() {
    }

    public CertifiedException(String message) {
        super(message);
    }

    public CertifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertifiedException(Throwable cause) {
        super(cause);
    }

    public CertifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
