package com.oursurvey.exception;

public class LoginPwdException extends RuntimeException {
    public LoginPwdException() {
    }

    public LoginPwdException(String message) {
        super(message);
    }

    public LoginPwdException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginPwdException(Throwable cause) {
        super(cause);
    }

    public LoginPwdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
