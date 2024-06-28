package com.my.bob.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.bob.constants.RespMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity {

    private String result;
    private int status = HttpStatus.OK.value();
    private Integer errorCode;
    private String message;
    private Object paging;
    private Object data;

    public ResponseEntity(Object data) {
        setData(data);
    }

    public void setError(HttpStatus httpStatus, String message) {
        setResult("ERROR");
        setStatus(httpStatus.value());
        setMessage(message);
    }


    public void setError(HttpStatus httpStatus, List<FieldError> fieldErrors) {
        setResult("ERROR");
        setStatus(httpStatus.value());

        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getField()).append("-").append(fieldError.getDefaultMessage());
        }

        setMessage(stringBuilder.toString());
    }

}