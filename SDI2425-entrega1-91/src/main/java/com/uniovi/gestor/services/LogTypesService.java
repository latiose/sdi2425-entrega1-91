package com.uniovi.gestor.services;

import org.springframework.stereotype.Service;

@Service
public class LogTypesService {
    private final static String PET = "PET";
    private final static String LOGIN_EX = "LOGIN-EX";
    private final static String LOGIN_ERR = "LOGIN-ERR";
    private final static String LOGOUT = "LOGOUT";

    private final String[] typeKeys = {
            PET,
            LOGIN_EX,
            LOGIN_ERR,
            LOGOUT
    };

    public String[] getLogTypes() {
        return typeKeys;
    }

}
