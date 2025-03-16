package com.uniovi.gestor;


import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleStatusConfig {

    public enum VehicleStatus {
        AVAILABLE, UNAVAILABLE
    }

}
