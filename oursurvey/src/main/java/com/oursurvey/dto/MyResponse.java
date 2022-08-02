package com.oursurvey.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MyResponse {
    public static final Integer STATUS_GOOD = 200;             // 정상
    public static final Integer CLIENT_ERROR = 400;            // 클라이언트에러(invalid form ...)
    public static final Integer DUPLICATE_EMAIL = 401;         // 회원가입시 이메일 중복 에러
    public static final Integer NOT_FOUND = 404;               // 404
    public static final Integer INVALID_TOKEN = 480;           // jwt 토큰 형식이 아님
    public static final Integer INVALID_ACCESSTOKEN = 481;     // jwt access token 에러
    public static final Integer INVALID_REFRESHTOKEN = 482;    // jwt refresh token 에러
    public static final Integer INVALID_ID = 483;              // 아이디 에러
    public static final Integer INVALID_ID_PWD_MATCHING = 484; // ID, PWD 매칭 에러
    public static final Integer INVALID_CERTIFIED_CODE = 485;  // 인증코드에러
    public static final Integer SERVER_ERROR = 500;            // 서버에러(db 등)

    private Integer code = STATUS_GOOD;
    private String message = null;
    private Object data = null;

    public MyResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public MyResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
