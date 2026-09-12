package com.commercex.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Base class for all business exceptions in CommerceX.
 */
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    private final HttpStatus status;

    protected BaseException(
            ErrorCode errorCode,
            HttpStatus status,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

}