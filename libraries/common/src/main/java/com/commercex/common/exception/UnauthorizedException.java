package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(
            ErrorCode errorCode,
            String message
    ) {

        super(
                errorCode,
                HttpStatus.UNAUTHORIZED,
                message
        );

    }

}