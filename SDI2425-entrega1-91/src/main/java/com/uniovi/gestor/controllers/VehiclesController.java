package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.FuelTypesService;
import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddVehicleFormValidator;
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
    private final AddVehicleFormValidator addVehicleFormValidator;
    private final FuelTypesService fuelTypesService;

    public VehiclesController(VehiclesService vehiclesService, AddVehicleFormValidator addVehicleFormValidator, FuelTypesService fuelTypesService) {
        this.vehiclesService = vehiclesService;
        this.addVehicleFormValidator = addVehicleFormValidator;
        this.fuelTypesService = fuelTypesService;
    }

    @RequestMapping(value = "/vehicle/add")
    public String getVehicle(Model model){
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypesList", fuelTypesService.getFuelTypes());
        return "vehicle/add";
    }

    @RequestMapping(value="/vehicle/add", method = RequestMethod.POST)
    public String addVehicle(@Validated Vehicle vehicle, BindingResult result, Model model){
        addVehicleFormValidator.validate(vehicle, result);
        model.addAttribute("vehicle", vehicle);
        if(result.hasErrors()){
            model.addAttribute("fuelTypesList", fuelTypesService.getFuelTypes());
            return "vehicle/add";
        }
        vehiclesService.addVehicle(vehicle);
        return "redirect:/vehicle/list";
    }

    @RequestMapping("/vehicle/list")
    public String getVehicleList(Model model){
        model.addAttribute("vehiclesList", vehiclesService.getVehicles());
        return "vehicle/list";
    }

    @RequestMapping("/vehicle/list/update")
    public String updateList(Model model){
        model.addAttribute("vehicleTable", vehiclesService.getVehicles() );
        return "vehicle/list :: vehicleTable";
    }
}
