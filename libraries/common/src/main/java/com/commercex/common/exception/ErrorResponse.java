package com.commercex.common.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(

        boolean success,

        String errorCode,

        String message,

        int status,

        String method,

        String path,

        String traceId,

        Instant timestamp,

        List<ValidationError> errors

) {
}