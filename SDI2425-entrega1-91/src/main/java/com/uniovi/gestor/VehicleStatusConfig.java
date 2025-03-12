package com.uniovi.gestor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleStatusConfig {
    public enum VehicleStatus {
        AVAILABLE, UNAVAILABLE
    }
    @Value("${vehicles.status.available}")
    private String availableStatus;

    @Value("${vehicles.status.unavailable}")
    private String unavailableStatus;

    public String getStatusDisplay(VehicleStatus status) {
        return switch (status) {
            case AVAILABLE -> availableStatus;
            case UNAVAILABLE -> unavailableStatus;
        };
    }
}
