package com.oursurvey.exception;

public class AlreadyReplySurveyException extends RuntimeException {
    public AlreadyReplySurveyException() {
    }

    public AlreadyReplySurveyException(String message) {
        super(message);
    }

    public AlreadyReplySurveyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyReplySurveyException(Throwable cause) {
        super(cause);
    }

    public AlreadyReplySurveyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
