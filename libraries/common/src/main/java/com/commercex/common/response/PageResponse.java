package com.commercex.common.response;

import java.time.Instant;
import java.util.List;

public record PageResponse<T>(
        boolean success,
        String message,
        List<T> data,

        int page,
        int size,

        long totalElements,
        int totalPages,

        boolean first,
        boolean last,

        boolean hasNext,
        boolean hasPrevious,

        Instant timestamp,
        String traceId
) {

    public static <T> PageResponse<T> of(
            List<T> data,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last,
            boolean hasNext,
            boolean hasPrevious,
            String message,
            String traceId
    ) {

        return new PageResponse<>(
                true,
                message,
                data,
                page,
                size,
                totalElements,
                totalPages,
                first,
                last,
                hasNext,
                hasPrevious,
                Instant.now(),
                traceId
        );
    }

}