package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Journey;


import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.repositories.VehiclesRepository;
import com.uniovi.gestor.services.JourneysService;

import com.uniovi.gestor.services.VehiclesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class JourneysController {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;
    private final VehiclesRepository vehiclesRepository;

    public JourneysController(JourneysService journeysService, VehiclesService vehiclesService, VehiclesRepository vehiclesRepository) {
        this.journeysService = journeysService;
        this.vehiclesService = vehiclesService;
        this.vehiclesRepository = vehiclesRepository;
    }

    @RequestMapping(value = "/journey/add")
    public String getJourney(Model model){
        model.addAttribute("journey", new Journey());
        model.addAttribute("plateList",vehiclesRepository.findAllPlates());
        return "journey/add";
    }

    @RequestMapping(value="/journey/add", method = RequestMethod.POST)
    public String addJourney(@ModelAttribute String plateNumber, BindingResult result, Model model){
        Vehicle v = vehiclesService.getVehicleByNumberPlate(plateNumber);
        Journey journey = new Journey(v);
        model.addAttribute("journey", journey);
        journeysService.addJourney(journey);
        return "redirect:/journey/list";
    }

    @RequestMapping("/journey/list")
    public String getJourneyList(Model model, Pageable pageable){
        Page<Journey> journeys = journeysService.getJourneys(pageable);
        model.addAttribute("journeyList",journeys);
        model.addAttribute("page", journeys);
        return "journey/list";
    }

    @RequestMapping("/journey/list/update")
    public String updateList(Model model, Pageable pageable){
        model.addAttribute("journeyList", journeysService.getJourneys(pageable) );
        return "journey/list :: journeyTable";
    }
}
