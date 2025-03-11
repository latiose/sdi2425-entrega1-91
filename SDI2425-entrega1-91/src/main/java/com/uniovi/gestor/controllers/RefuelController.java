package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.RefuelsService;
import com.uniovi.gestor.validators.AddRefuelFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RefuelController {

    private final RefuelsService refuelsService;
    private final AddRefuelFormValidator addRefuelFormValidator;

    public RefuelController(RefuelsService refuelsService, AddRefuelFormValidator addRefuelFormValidator) {
        this.refuelsService = refuelsService;
        this.addRefuelFormValidator = addRefuelFormValidator;
    }

    @RequestMapping(value = "/refuel/add")
    public String getRefuel(Model model){
        model.addAttribute("refuel", new Refuel());
        return "refuel/add";
    }

    @RequestMapping(value="/refuel/add", method = RequestMethod.POST)
    public String addRefuel(@Validated Refuel refuel, BindingResult result, Model model){
        addRefuelFormValidator.validate(refuel, result);
        model.addAttribute("refuel", refuel);
        if(result.hasErrors()){
            return "refuel/add";
        }
        refuelsService.addRefuel(refuel);
        return "redirect:/refuel/list";
    }
}
