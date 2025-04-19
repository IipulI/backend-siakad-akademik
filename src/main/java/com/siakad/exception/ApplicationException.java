package com.siakad.exception;

import com.siakad.enums.ExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException{

    private ExceptionType type;

    public ApplicationException(ExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public ApplicationException(ExceptionType type, String customMessage) {
        super(customMessage);
        this.type = type;
    }

    public ApplicationException(HttpStatus httpStatus, String message) {
        super(message);
    }
}
