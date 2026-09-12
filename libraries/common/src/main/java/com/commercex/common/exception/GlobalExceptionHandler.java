package com.commercex.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(

            MethodArgumentNotValidException ex,

            HttpServletRequest request

    ) {

        List<ValidationError> errors =

                ex.getBindingResult()

                        .getFieldErrors()

                        .stream()

                        .map(this::toValidationError)

                        .toList();

        ErrorResponse response =

                new ErrorResponse(

                        false,

                        "VALIDATION_ERROR",

                        "Validation Failed",

                        400,

                        request.getMethod(),

                        request.getRequestURI(),

                        MDC.get("traceId"),

                        Instant.now(),

                        errors

                );

        return ResponseEntity.badRequest()

                .body(response);

    }




    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(

            BaseException ex,

            HttpServletRequest request

    ) {

        log.error(
                "Business Exception : {}",
                ex.getMessage(),
                ex
        );

        ErrorResponse response =

                new ErrorResponse(

                        false,

                        ex.getErrorCode().name(),

                        ex.getMessage(),

                        ex.getStatus().value(),

                        request.getMethod(),

                        request.getRequestURI(),

                        MDC.get("traceId"),

                        Instant.now(),

                        List.of()

                );

        return ResponseEntity

                .status(ex.getStatus())

                .body(response);

    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(

            Exception ex,

            HttpServletRequest request

    ) {

        log.error("Unhandled Exception", ex);

        ErrorResponse response =

                new ErrorResponse(

                        false,

                        "INTERNAL_SERVER_ERROR",

                        "Something went wrong.",

                        500,

                        request.getMethod(),

                        request.getRequestURI(),

                        MDC.get("traceId"),

                        Instant.now(),

                        List.of()

                );

        return ResponseEntity

                .status(HttpStatus.INTERNAL_SERVER_ERROR)

                .body(response);

    }



    private ValidationError toValidationError(
            FieldError field
    ) {

        return new ValidationError(
                field.getField(),
                field.getDefaultMessage()
        );

    }

}