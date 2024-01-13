package com.springsecurity.exception;

import com.springsecurity.constant.Error;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final Error error;

    public CommonException(Error error) {
        super(error.getMessage());
        this.error = error;
    }
}
