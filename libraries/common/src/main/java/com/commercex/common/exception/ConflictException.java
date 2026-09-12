package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException(
            ErrorCode errorCode,
            String message
    ) {

        super(
                errorCode,
                HttpStatus.CONFLICT,
                message
        );

    }

}