package com.uniovi.gestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
