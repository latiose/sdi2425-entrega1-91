package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.RefuelsService;
import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddRefuelFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class RefuelsController {

    private final RefuelsService refuelsService;
    private final AddRefuelFormValidator addRefuelFormValidator;
    private final VehiclesService vehiclesService;
    private final LogService logService;

    public RefuelsController(RefuelsService refuelsService, AddRefuelFormValidator addRefuelFormValidator, VehiclesService vehiclesService, LogService logService) {
        this.refuelsService = refuelsService;
        this.addRefuelFormValidator = addRefuelFormValidator;
        this.vehiclesService = vehiclesService;
        this.logService = logService;
    }

    @RequestMapping(value = "/refuel/add")
    public String getRefuel(Model model){
        logService.log("PET", "PET [GET] /refuel/add");
        model.addAttribute("refuel", new Refuel());
        return "refuel/add";
    }

    @RequestMapping(value="/refuel/add", method = RequestMethod.POST)
    public String addRefuel(@Validated @ModelAttribute("refuel") Refuel refuel, BindingResult result, Model model){
        addRefuelFormValidator.validate(refuel, result);
        refuel.setDate(LocalDateTime.now());
        if(result.hasErrors()){
            logService.log("PET", "PET [POST] /refuel/add | INVALID | parameters: REFUEL = " + refuel.toString());
            return "refuel/add";
        }
        logService.log("PET", "PET [POST] /refuel/add | parameters: REFUEL = " + refuel.toString());
        refuelsService.addRefuel(refuel);
        return "redirect:/refuel/list";
    }

    @RequestMapping("/refuel/list")
    public String getVehicleList(Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /refuel/list | parameters: PAGE = " + pageable.getPageNumber());
        String numberPlate = vehiclesService.findAllPlates().get(0);
        Page<Refuel> refuels = refuelsService.getRefuelsByNumberPlate(numberPlate, pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("refuelsList",refuels);
        model.addAttribute("page", refuels);
        return "refuel/list";
    }

    @RequestMapping("/refuel/list/{plateNumber}")
    public String getVehicleList(@PathVariable("plateNumber") String plateNumber, Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /refuel/list/"
                + plateNumber + " | parameters: PLATE = "
                + plateNumber
                + ", PAGE = " + pageable.getPageNumber());
        Page<Refuel> refuels = refuelsService.getRefuelsByNumberPlate(plateNumber, pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("refuelsList",refuels);
        model.addAttribute("page", refuels);
        return "refuel/listVehicle";
    }

    @RequestMapping("/refuel/list/update")
    public String updateList(@RequestParam("plateNumber") String numberPlate, Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /refuel/list/update | parameters: PLATE = "
                + numberPlate
                + ", PAGE = " + pageable.getPageNumber());
        model.addAttribute("refuelsList", refuelsService.getRefuelsByNumberPlate(numberPlate, pageable) );
        return "refuel/list :: refuelsTable";
    }
}
