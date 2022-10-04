package com.oursurvey.exception;

public class LoginIdException extends RuntimeException {
    public LoginIdException() {
    }

    public LoginIdException(String message) {
        super(message);
    }

    public LoginIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginIdException(Throwable cause) {
        super(cause);
    }

    public LoginIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
