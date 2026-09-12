package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {

        super(
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                message
        );

    }

}