package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.RefuelsService;
import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddRefuelFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class RefuelsController {

    private final RefuelsService refuelsService;
    private final AddRefuelFormValidator addRefuelFormValidator;
    private final VehiclesService vehiclesService;

    public RefuelsController(RefuelsService refuelsService, AddRefuelFormValidator addRefuelFormValidator, VehiclesService vehiclesService) {
        this.refuelsService = refuelsService;
        this.addRefuelFormValidator = addRefuelFormValidator;
        this.vehiclesService = vehiclesService;
    }

    @RequestMapping(value = "/refuel/add")
    public String getRefuel(Model model){
        model.addAttribute("refuel", new Refuel());
        return "refuel/add";
    }

    @RequestMapping(value="/refuel/add", method = RequestMethod.POST)
    public String addRefuel(@Validated Refuel refuel, BindingResult result, Model model){
        addRefuelFormValidator.validate(refuel, result);
        refuel.setDate(LocalDateTime.now());
        model.addAttribute("refuel", refuel);
        if(result.hasErrors()){
            return "refuel/add";
        }
        refuelsService.addRefuel(refuel);
        return "redirect:/refuel/list";
    }

    @RequestMapping("/refuel/list")
    public String getVehicleList(Model model, Pageable pageable){
        String numberPlate = vehiclesService.findAllPlates().get(0);
        Page<Refuel> refuels = refuelsService.getRefuelsByNumberPlate(numberPlate, pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("refuelsList",refuels);
        model.addAttribute("page", refuels);
        return "refuel/list";
    }

    @RequestMapping("/refuel/list/update")
    public String updateList(@RequestParam("plateNumber") String numberPlate, Model model, Pageable pageable){
        model.addAttribute("refuelsList", refuelsService.getRefuelsByNumberPlate(numberPlate, pageable) );
        return "refuel/list :: refuelsTable";
    }
}
