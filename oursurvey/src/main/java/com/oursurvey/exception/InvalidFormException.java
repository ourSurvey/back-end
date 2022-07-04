package com.oursurvey.exception;

public class InvalidFormException extends RuntimeException {
    public InvalidFormException() {
    }

    public InvalidFormException(String message) {
        super(message);
    }

    public InvalidFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFormException(Throwable cause) {
        super(cause);
    }

    public InvalidFormException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
