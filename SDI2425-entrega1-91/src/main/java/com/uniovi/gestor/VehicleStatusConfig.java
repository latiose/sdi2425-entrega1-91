package com.uniovi.gestor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class VehicleStatusConfig {
    public enum VehicleStatus {
        AVAILABLE, UNAVAILABLE
    }
    @Autowired
    private MessageSource messageSource;

    public String getStatusDisplay(VehicleStatus status) {
        String key = switch (status) {
            case AVAILABLE -> "vehicles.status.available";
            case UNAVAILABLE -> "vehicles.status.unavailable";
        };
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
