package com.springsecurity.exception.handler;

import com.springsecurity.constant.Error;
import com.springsecurity.model.ErrorResponse;
import com.springsecurity.exception.CommonException;
import com.springsecurity.util.I18nMessageUtil;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        var validations = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> I18nMessageUtil.getLocalizedMessage(error.getDefaultMessage())));

        var errorResponse = new ErrorResponse(Error.INVALID_REQUEST, validations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> commonException(CommonException ex) {
        var errorResponse = new ErrorResponse(ex.getError());
        return ResponseEntity.status(ex.getError().getStatus()).body(errorResponse);
    }

}
