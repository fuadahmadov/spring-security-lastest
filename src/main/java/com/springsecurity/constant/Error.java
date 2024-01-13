package com.springsecurity.constant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.springsecurity.util.I18nMessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    INVALID_REQUEST("1", "error.invalid-request", BAD_REQUEST),
    INVALID_USER_CREDENTIAL("2", "error.invalid-user-credential", BAD_REQUEST),
    REGISTRATION_ALREADY_EXIST("3", "error.registration-already-exist", BAD_REQUEST),
    INVALID_TOKEN("4", "error.invalid-token", BAD_REQUEST),
    UNAUTHORIZED("5", "error.unauthorized", BAD_REQUEST),
    ACCESS_DENIED("6", "error.access-denied", BAD_REQUEST),
    INVALID_EMAIL_PASSWORD("7", "error.invalid-email-password", BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    public String getMessage() {
        return I18nMessageUtil.getLocalizedMessage(this.message);
    }
}
