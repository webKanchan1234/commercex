package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Generic business rule violation.
 */
public class BusinessException extends BaseException {

    public BusinessException(ErrorCode errorCode, HttpStatus status, String message) {
        super(errorCode, status, message);
    }

}