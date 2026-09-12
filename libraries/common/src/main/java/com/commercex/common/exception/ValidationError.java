package com.commercex.common.exception;

public record ValidationError(

        String field,

        String message

) {
}