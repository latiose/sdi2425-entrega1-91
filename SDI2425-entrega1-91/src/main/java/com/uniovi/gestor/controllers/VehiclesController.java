package com.uniovi.gestor.controllers;

import com.uniovi.gestor.VehicleStatusConfig;
import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.FuelTypesService;
import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddVehicleFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VehiclesController {

    private final VehiclesService vehiclesService;
    private final AddVehicleFormValidator addVehicleFormValidator;
    private final FuelTypesService fuelTypesService;
    private final EmployeesService employeesService;
    private final LogService logService;

    public VehiclesController(VehiclesService vehiclesService, AddVehicleFormValidator addVehicleFormValidator, FuelTypesService fuelTypesService, EmployeesService employeesService, LogService logService) {
        this.vehiclesService = vehiclesService;
        this.addVehicleFormValidator = addVehicleFormValidator;
        this.fuelTypesService = fuelTypesService;
        this.employeesService = employeesService;
        this.logService = logService;
    }

    @RequestMapping(value = "/vehicle/add")
    public String getVehicle(Model model){
        logService.log("PET", "PET [GET] /vehicle/add");
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fuelTypesList", fuelTypesService.getFuelTypes());
        return "vehicle/add";
    }

    @RequestMapping(value="/vehicle/add", method = RequestMethod.POST)
    public String addVehicle(@Validated Vehicle vehicle, BindingResult result, Model model){

        addVehicleFormValidator.validate(vehicle, result);
        model.addAttribute("vehicle", vehicle);
        if(result.hasErrors()){
            logService.log("PET", "PET [POST] | INVALID | /vehicle/add | parameters: VEHICLE = "
                    + vehicle);
            model.addAttribute("fuelTypesList", fuelTypesService.getFuelTypes());
            return "vehicle/add";
        }
        vehicle.setStatus(VehicleStatusConfig.VehicleStatus.AVAILABLE);
        logService.log("PET", "PET [POST] /vehicle/add | parameters: VEHICLE = "
                + vehicle);
        vehiclesService.addVehicle(vehicle);
        return "redirect:/vehicle/list";
    }

    @RequestMapping("/vehicle/list")
    public String getVehicleList(Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /vehicle/list | parameters: PAGE = "
                + pageable.getPageNumber());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();

        Employee employee = employeesService.getEmployeeByDni(dni);

        if(employee.getRole().equals("ROLE_STANDARD")){
            Page<Vehicle> vehicles = vehiclesService.findVehiclesByStatus(VehicleStatusConfig.VehicleStatus.AVAILABLE, pageable);
            model.addAttribute("vehiclesList",vehicles);
            model.addAttribute("page", vehicles);
        }
        else{
            Page<Vehicle> vehicles = vehiclesService.getVehicles(pageable);
            model.addAttribute("vehiclesList",vehicles);
            model.addAttribute("page", vehicles);
        }


        return "vehicle/list";
    }

    @RequestMapping("/vehicle/list/update")
    public String updateList(Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /vehicle/list/update | parameters: PAGE = "
                + pageable.getPageNumber());
        model.addAttribute("vehiclesList", vehiclesService.getVehicles(pageable) );
        return "vehicle/list :: vehicleTable";
    }

    @RequestMapping(value = "/vehicle/delete", method = RequestMethod.POST)
    public String delete(@RequestBody List<Long> vehicleIds) {
        List<String> deletedPlates = new ArrayList<>();
        vehicleIds.forEach(
                vehicleId -> {deletedPlates.add(
                    vehiclesService.getVehicle(vehicleId)
                                   .getNumberPlate()
                    );
                });
        logService.log("PET", "PET [POST] /vehicle/list/delete " +
                "| parameters: VEHICLE_IDS (number plates deleted) = "
                + deletedPlates);
        if (!vehicleIds.isEmpty()) {
            vehicleIds.forEach(vehiclesService::deleteVehicle);
        }
        return "redirect:/vehicle/list";
    }
}
