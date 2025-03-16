package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Log;
import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.LogTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LogsController {
    private final LogService logService;
    private final LogTypesService logTypesService;

    public LogsController(LogService logService, LogTypesService logTypesService) {
        this.logService = logService;
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

    @RequestMapping(value="/logs/delete", method = RequestMethod.POST)
    public String delete(@RequestBody String type){
        List<Log> logs = logService.findByType(type.split("=")[1]);

        if(!logs.isEmpty()){
            logService.log("PET", "PET [POST] /log/delete " +
                    "| parameters: LOG TYPES (log types deleted) = " + type);

            for(Log log : logs){
                logService.delete(log.getId());
            }
        }



        return "logs/list";
    }

}
