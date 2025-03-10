package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Journey;


import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.JourneysService;

import com.uniovi.gestor.services.VehiclesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class JourneysController {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;
    private final EmployeesService employeesService;

    public JourneysController(JourneysService journeysService, VehiclesService vehiclesService, EmployeesService employeesService) {
        this.journeysService = journeysService;
        this.vehiclesService = vehiclesService;
        this.employeesService = employeesService;
    }

    @RequestMapping(value = "/journey/add")
    public String getJourney(Model model){
        model.addAttribute("journey", new Journey());
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        return "journey/add";
    }

    @RequestMapping(value="/journey/add", method = RequestMethod.POST)
    public String addJourney(@RequestParam("plateNumber") String plateNumber, Model model) {
        Vehicle v = vehiclesService.getVehicleByNumberPlate(plateNumber);
        if (v == null) {
            model.addAttribute("journey", new Journey());
            model.addAttribute("plateList", vehiclesService.findAllPlates());
            model.addAttribute("error", "Vehiculo no encontrado");
            return "journey/add";
        }

        List<Journey> vehicleJourneys = journeysService.findByVehicle(v);
        for (Journey j : vehicleJourneys) {
            if (j.getEndDate() == null) {
                model.addAttribute("journey", new Journey());
                model.addAttribute("plateList", vehiclesService.findAllPlates());
                model.addAttribute("error", "Vehiculo en uso");
                return "journey/add";
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        List<Journey> userJourneys = journeysService.findByDni(dni);
        for (Journey j : userJourneys) {
            if (j.getEndDate() == null) {
                model.addAttribute("journey", new Journey());
                model.addAttribute("plateList", vehiclesService.findAllPlates());
                model.addAttribute("error", "Ya tienes un trayecto activo");
                return "journey/add";
            }
        }

        Journey journey = new Journey(v);
        Vehicle existingVehicle = journeysService.findVehicleByNumberPlate(plateNumber);
        if (existingVehicle != null) {
            journey.setOdometerStart(existingVehicle.getMileage());
        } else {
            journey.setOdometerStart(0);
        }
        journey.setEmployee(employeesService.getEmployeeByDni(dni));
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
