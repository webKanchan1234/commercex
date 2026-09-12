package com.commercex.common.exception;

public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(500),
    INVALID_REQUEST(400),
    VALIDATION_FAILED(400),
    RESOURCE_NOT_FOUND(404),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    CONFLICT(409),

    BAD_REQUEST(402),

    VALIDATION_ERROR(500),



    // User
    USER_NOT_FOUND(404),
    USER_ALREADY_EXISTS(409),
    INVALID_CREDENTIALS(401),

    // Product
    PRODUCT_NOT_FOUND(404),
    PRODUCT_ALREADY_EXISTS(409),

    // Order
    ORDER_NOT_FOUND(404),
    ORDER_ALREADY_CANCELLED(409),

    // Inventory
    INSUFFICIENT_STOCK(409),

    // Payment
    PAYMENT_FAILED(400),
    BUSINESS_VALIDATION_FAILED(404);


    private final int status;

    ErrorCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}