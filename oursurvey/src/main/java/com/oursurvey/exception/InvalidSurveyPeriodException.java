package com.oursurvey.exception;

public class InvalidSurveyPeriodException extends RuntimeException {
    public InvalidSurveyPeriodException() {
    }

    public InvalidSurveyPeriodException(String message) {
        super(message);
    }

    public InvalidSurveyPeriodException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSurveyPeriodException(Throwable cause) {
        super(cause);
    }

    public InvalidSurveyPeriodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
