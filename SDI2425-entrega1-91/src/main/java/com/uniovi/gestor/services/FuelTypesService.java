package com.uniovi.gestor.services;
import org.springframework.stereotype.Service;

@Service
public class FuelTypesService {

    private final String[] typeKeys = {
            "GASOLINA",
            "DIESEL",
            "MICROHIBRIDO",
            "HIBRIDO",
            "ELECTRICO",
            "GLP",
            "GNL"
    };

    public String[] getFuelTypes() {
        return typeKeys;
    }
}
