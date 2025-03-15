package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Log;
import com.uniovi.gestor.services.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final LogService logService;

    public HomeController(LogService logService) {
        this.logService = logService;
    }

    @RequestMapping("/")
    public String index() {
        logService.log("PET", "PET [GET] /");
        return "index";
    }

    @RequestMapping("/error-403")
    public String error403() {
        logService.log("PET", "PET [GET] /error-403");
        return "error/error-403";
    }

}
