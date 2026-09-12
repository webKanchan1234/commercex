package com.commercex.common.response;

import java.time.Instant;

/**
 * Standard success response returned by all REST APIs.
 *
 * @param success  Indicates whether the request was successful.
 * @param message  Human-readable message.
 * @param data     Response payload.
 * @param timestamp Server timestamp (UTC).
 * @param traceId  Distributed tracing identifier.
 */
public record ApiSuccessResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp,
        String traceId
) {

    /**
     * Success response with default message.
     */
    public static <T> ApiSuccessResponse<T> success(T data) {
        return new ApiSuccessResponse<>(
                true,
                "Request processed successfully",
                data,
                Instant.now(),
                null
        );
    }

    /**
     * Success response with custom message.
     */
    public static <T> ApiSuccessResponse<T> success(String message, T data) {
        return new ApiSuccessResponse<>(
                true,
                message,
                data,
                Instant.now(),
                null
        );
    }

    /**
     * Success response without payload.
     */
    public static ApiSuccessResponse<Void> success(String message) {
        return new ApiSuccessResponse<>(
                true,
                message,
                null,
                Instant.now(),
                null
        );
    }

//    public static ApiResponse<Void> success(String message){
//
//        return new ApiResponse<>(
//                true,
//                "Success",
//                null,
//                Instant.now(),
//                null
//        );
//
//    }

}