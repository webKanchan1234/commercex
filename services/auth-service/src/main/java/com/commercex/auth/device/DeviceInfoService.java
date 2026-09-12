package com.commercex.auth.device;

import jakarta.servlet.http.HttpServletRequest;

public interface DeviceInfoService {

    DeviceInfo extractDeviceInfo(
            HttpServletRequest request
    );

}