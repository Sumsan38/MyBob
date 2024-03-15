package com.my.bob.handler;

import com.my.bob.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public CommonResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e, WebRequest request) {
        CommonResponse commonResponse = new CommonResponse();

        commonResponse.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        return commonResponse;
    }

}
