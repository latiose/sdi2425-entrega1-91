package com.uniovi.gestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FuelTypesService {
    @Autowired
    private MessageSource messageSource;

    private final String[] typeKeys = {
            "fuel.type.gasoline",
            "fuel.type.diesel",
            "fuel.type.microhybrid",
            "fuel.type.hybrid",
            "fuel.type.electric",
            "fuel.type.lpg",
            "fuel.type.lng"
    };

    public String[] getFuelTypes() {
        String[] fuelTypes = new String[typeKeys.length];
        for(String key : typeKeys) {
            fuelTypes[Integer.parseInt(key)] = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        }
        return fuelTypes;
    }
}
