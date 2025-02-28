package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.VehiclesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VehiclesController {

    private final VehiclesService vehiclesService;
    // añadir un validador para los formularios

    public VehiclesController(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @RequestMapping(value = "/vehicle/add")
    public String getVehicle(Model model){
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle/add";
    }

    @RequestMapping(value="/vehicle/add", method = RequestMethod.POST)
    public String addVehicle(@Validated Vehicle vehicle, BindingResult result, Model model){

        vehiclesService.addVehicle(vehicle);
        return "redirect:/vehicle/list";
    }
}
