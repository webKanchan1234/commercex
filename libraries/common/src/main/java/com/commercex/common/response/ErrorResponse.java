package com.commercex.common.response;

import java.time.Instant;

public record ErrorResponse(
        boolean success,
        String errorCode,
        String message,
        Instant timestamp,
        String traceId
) {

    public static ErrorResponse of(
            String errorCode,
            String message,
            String traceId
    ) {
        return new ErrorResponse(
                false,
                errorCode,
                message,
                Instant.now(),
                traceId
        );
    }
}