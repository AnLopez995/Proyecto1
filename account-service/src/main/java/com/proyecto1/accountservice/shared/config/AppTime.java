package com.proyecto1.accountservice.shared.config;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class AppTime {

    private static final ZoneId APP_ZONE_ID = ZoneId.of("America/Bogota");

    private AppTime() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(APP_ZONE_ID);
    }
}