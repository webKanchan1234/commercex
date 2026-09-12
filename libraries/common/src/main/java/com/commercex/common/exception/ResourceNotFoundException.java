package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(
            ErrorCode errorCode,
            String message
    ) {

        super(
                errorCode,
                HttpStatus.NOT_FOUND,
                message
        );

    }



}