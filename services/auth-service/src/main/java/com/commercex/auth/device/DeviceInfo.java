package com.commercex.auth.device;

import lombok.Builder;

@Builder
public record DeviceInfo(

        String browser,

        String operatingSystem,

        String deviceType,

        String deviceName,

        String ipAddress,

        String userAgent

) {
}