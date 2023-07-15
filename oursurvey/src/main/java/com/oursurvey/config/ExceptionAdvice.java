package com.oursurvey.config;

import com.oursurvey.dto.MyResponse;
import com.oursurvey.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            AuthFailException.class,
            PointLackException.class,
            DuplicateEmailException.class,
            LoginIdException.class,
            LoginPwdException.class,
            InvalidFormException.class,
            InvalidTokenException.class,
            InvalidAccessTokenException.class,
            InvalidRefreshTokenException.class,
            CertifiedException.class,
            S3FileUploadException.class,
            InvalidSurveyPeriodException.class,
            AlreadyReplySurveyException.class,
            ObjectNotFoundException.class,
            Exception.class
    })
    public MyResponse exceptionFor400(Exception e) {
        MyResponse myResponse = new MyResponse().setCode(MyResponse.CLIENT_ERROR).setMessage(e.getMessage());
        if (e instanceof DuplicateEmailException) myResponse.setCode(MyResponse.DUPLICATE_EMAIL);
        if (e instanceof LoginIdException) myResponse.setCode(MyResponse.LOGIN_ID);
        if (e instanceof LoginPwdException) myResponse.setCode(MyResponse.LOGIN_PWD);
        if (e instanceof PointLackException) myResponse.setCode(MyResponse.POINT_LACK);
        if (e instanceof InvalidTokenException) myResponse.setCode(MyResponse.INVALID_TOKEN);
        if (e instanceof InvalidAccessTokenException) myResponse.setCode(MyResponse.INVALID_ACCESSTOKEN);
        if (e instanceof InvalidRefreshTokenException) myResponse.setCode(MyResponse.INVALID_REFRESHTOKEN);
        if (e instanceof CertifiedException) myResponse.setCode(MyResponse.INVALID_CERTIFIED_CODE);
        if (e instanceof S3FileUploadException) myResponse.setCode(MyResponse.S3_FILE_UPLOAD);
        if (e instanceof InvalidSurveyPeriodException) myResponse.setCode(MyResponse.INVALID_SURVEY_PERIOD);
        if (e instanceof ObjectNotFoundException) myResponse.setCode(MyResponse.CLIENT_ERROR);

        return myResponse;
    }
}
