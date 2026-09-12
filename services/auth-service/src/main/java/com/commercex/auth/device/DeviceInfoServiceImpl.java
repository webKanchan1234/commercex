package com.commercex.auth.device;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class DeviceInfoServiceImpl
        implements DeviceInfoService {

    @Override
    public DeviceInfo extractDeviceInfo(
            HttpServletRequest request
    ) {

        String userAgent = request.getHeader("User-Agent");

        return DeviceInfo.builder()

                .browser(
                        detectBrowser(userAgent)
                )

                .operatingSystem(
                        detectOS(userAgent)
                )

                .deviceType(
                        detectDevice(userAgent)
                )

                .deviceName(
                        detectDeviceName(userAgent)
                )

                .ipAddress(
                        extractIp(request)
                )

                .userAgent(userAgent)

                .build();

    }

    private String extractIp(
            HttpServletRequest request
    ) {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null &&
                !forwarded.isBlank()) {

            return forwarded.split(",")[0];

        }

        return request.getRemoteAddr();

    }

    private String detectBrowser(
            String userAgent
    ) {

        if (userAgent == null)
            return "Unknown";

        if (userAgent.contains("Edg"))
            return "Edge";

        if (userAgent.contains("Chrome"))
            return "Chrome";

        if (userAgent.contains("Firefox"))
            return "Firefox";

        if (userAgent.contains("Safari"))
            return "Safari";

        return "Unknown";

    }

    private String detectOS(
            String userAgent
    ) {

        if (userAgent == null)
            return "Unknown";

        if (userAgent.contains("Windows"))
            return "Windows";

        if (userAgent.contains("Mac"))
            return "macOS";

        if (userAgent.contains("Linux"))
            return "Linux";

        if (userAgent.contains("Android"))
            return "Android";

        if (userAgent.contains("iPhone"))
            return "iOS";

        return "Unknown";

    }

    private String detectDevice(
            String userAgent
    ) {

        if (userAgent == null)
            return "Desktop";

        if (userAgent.contains("Mobile"))
            return "Mobile";

        if (userAgent.contains("Tablet"))
            return "Tablet";

        return "Desktop";

    }

    private String detectDeviceName(
            String userAgent
    ) {

        return detectBrowser(userAgent)
                + " on "
                + detectOS(userAgent);

    }

}