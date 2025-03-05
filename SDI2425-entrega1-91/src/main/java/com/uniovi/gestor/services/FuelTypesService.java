package com.uniovi.gestor.services;

import org.springframework.stereotype.Service;

@Service
public class FuelTypesService {
    String[] types ={"GASOLINA", "DIESEL", "MICROHIBRIDO", "HIBRIDO", "ELECTRICO","GLP","GNL"};

    public Object getFuelTypes() {
        return types;
    }
}
