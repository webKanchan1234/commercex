package com.commercex.common.util;

import org.slf4j.MDC;

public final class TraceIdUtils {

    private static final String TRACE_ID = "traceId";

    private TraceIdUtils() {
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
}