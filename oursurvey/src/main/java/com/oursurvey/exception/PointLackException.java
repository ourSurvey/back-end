package com.oursurvey.exception;

public class PointLackException extends RuntimeException {
    public PointLackException() {
    }

    public PointLackException(String message) {
        super(message);
    }

    public PointLackException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointLackException(Throwable cause) {
        super(cause);
    }

    public PointLackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
