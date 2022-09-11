package com.oursurvey.exception;

public class PkMangagerTableException extends RuntimeException {
    public PkMangagerTableException() {
    }

    public PkMangagerTableException(String message) {
        super(message);
    }

    public PkMangagerTableException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkMangagerTableException(Throwable cause) {
        super(cause);
    }

    public PkMangagerTableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
