package com.uniovi.gestor.controllers;

import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.LogTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogsController {
    private final LogService logService;
    private final LogTypesService logsTypeService;
    private final LogTypesService logTypesService;

    public LogsController(LogService logService, LogTypesService logsTypeService, LogTypesService logTypesService) {
        this.logService = logService;
        this.logsTypeService = logsTypeService;
        this.logTypesService = logTypesService;
    }

    @RequestMapping("/logs/list")
    public String getLogsList(@RequestParam(value = "type", required = false) String type, Model model){
        logService.log("PET", "PET [GET] /logs/list");
        model.addAttribute("logTypes", logTypesService.getLogTypes());
        model.addAttribute("selectedType", type);
        model.addAttribute("logs", type != null ?
                logService.findByType(type) :
                logService.getOrderedLogs());

        return "logs/list";
    }

}
