package com.uniovi.gestor.controllers;

import com.uniovi.gestor.repositories.LogRepository;
import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.LogTypesService;
import com.uniovi.gestor.services.VehiclesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LogsController {
    private final LogService logService;
    private final LogTypesService logsTypeService;
    private final LogTypesService logTypesService;
    private final LogRepository logRepository;
    private final VehiclesService vehiclesService;

    public LogsController(LogService logService, LogTypesService logsTypeService, LogTypesService logTypesService, LogRepository logRepository, VehiclesService vehiclesService) {
        this.logService = logService;
        this.logsTypeService = logsTypeService;
        this.logTypesService = logTypesService;
        this.logRepository = logRepository;
        this.vehiclesService = vehiclesService;
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

    @RequestMapping(value="/logs/delete", method = RequestMethod.POST)
    public String delete(@RequestBody List<Long> logIds){
        List<Long> deletedLogIds = new ArrayList<>();
        for(Long logId : logIds){
            deletedLogIds.add(logRepository.findById(logId).get().getId());
        }

        logService.log("PET", "PET [POST] /log/delete " +
                "| parameters: LOG_IDS (log ids deleted) = " + deletedLogIds.toString());

        if(!deletedLogIds.isEmpty()){
            deletedLogIds.forEach(logService::delete);
        }

        return "logs/list";
    }

}
