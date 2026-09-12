package com.commercex.common.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    private DateUtils() {
    }

    public static Instant now() {
        return Instant.now();
    }

    public static String nowAsString() {
        return ISO_FORMATTER.format(Instant.now());
    }

    public static String format(Instant instant) {
        return ISO_FORMATTER.format(instant);
    }
}