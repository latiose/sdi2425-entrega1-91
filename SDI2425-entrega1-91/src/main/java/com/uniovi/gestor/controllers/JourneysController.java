package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Journey;


import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.JourneysService;

import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddJourneyFormValidator;
import com.uniovi.gestor.validators.EndJourneyFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class JourneysController {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;
    private final EmployeesService employeesService;
    private final EndJourneyFormValidator endJourneyFormValidator;
    private final AddJourneyFormValidator addJourneyFormValidator;


    public JourneysController(JourneysService journeysService, VehiclesService vehiclesService, EndJourneyFormValidator endJourneyFormValidator, EmployeesService employeeService, AddJourneyFormValidator addJourneyFormValidator) {
        this.journeysService = journeysService;
        this.vehiclesService = vehiclesService;
        this.employeesService = employeeService;
        this.endJourneyFormValidator = endJourneyFormValidator;
        this.addJourneyFormValidator = addJourneyFormValidator;
    }

    @RequestMapping(value = "/journey/add")
    public String getJourney(Model model){
        model.addAttribute("journey", new Journey());
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        return "journey/add";
    }

    @RequestMapping(value = "/journey/end")
    public String getJourneyEnd(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Journey journey = journeysService.findActiveJourneyByDni(dni);
        if (journey == null) {
            return "redirect:/journey/list";
        }
        model.addAttribute("journey", journey);
        return "journey/end";
    }

    @RequestMapping(value="/journey/end", method = RequestMethod.POST)
    public String endJourney(@Validated @ModelAttribute("journey") Journey journey, BindingResult result) {
        endJourneyFormValidator.validate(journey,result);
        if (result.hasErrors()) {
            return "journey/end";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Journey existingJourney = journeysService.findActiveJourneyByDni(dni);
        Vehicle existingVehicle = journeysService.findVehicleByJourney(existingJourney.getId());
        existingVehicle.setMileage((float) journey.getOdometerEnd());
        existingJourney.setOdometerEnd(journey.getOdometerEnd());
        existingJourney.setObservations(journey.getObservations());
        existingJourney.setEndDate(LocalDateTime.now());
        journeysService.addJourney(existingJourney);
        return "redirect:/journey/list"; //Actualizarlo cuando se acabe el 13
    }

    @RequestMapping(value="/journey/add", method = RequestMethod.POST)
    public String addJourney(@RequestParam("plateNumber") String plateNumber,
                             @ModelAttribute("journey") Journey journey,
                             BindingResult result, Model model) {

        Vehicle vehicle = vehiclesService.findVehicleByNumberPlate(plateNumber);
        if (vehicle == null) {
            result.rejectValue("vehicle.numberPlate", "Error.empty");
        } else {
            journey.setVehicle(vehicle);
        }

        addJourneyFormValidator.validate(journey, result);

        if (result.hasErrors()) {
            model.addAttribute("plateList", vehiclesService.findAllPlates());
            model.addAttribute("journey", journey);
            return "journey/add";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Vehicle existingVehicle = journeysService.findVehicleByNumberPlate(plateNumber);
        if (existingVehicle != null) {
            journey.setOdometerStart(existingVehicle.getMileage());
        } else {
            journey.setOdometerStart(0);
        }
        journey.setEmployee(employeesService.getEmployeeByDni(dni));
        journeysService.addJourney(journey);
        return "redirect:/journey/list"; //Actualizar
    }


    @RequestMapping("/journey/list")
    public String getJourneyList(Model model, Pageable pageable){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Page<Journey> journeys = journeysService.findByDniPage(dni,pageable);
        model.addAttribute("journeyList",journeys.getContent());
        model.addAttribute("page", journeys);
        return "journey/list";
    }

    @RequestMapping("/journey/list/update")
    public String updateList(Model model, Pageable pageable){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Page<Journey> journeys = journeysService.findByDniPage(dni,pageable);
        model.addAttribute("journeyList",journeys.getContent());
        return "journey/list :: journeyTable";
    }

    @RequestMapping("/journey/list/vehicle")
    public String getVehicleList(Model model, Pageable pageable){
        String numberPlate = vehiclesService.findAllPlates().get(0);
        Page<Journey> journeys = journeysService.findByVehiclePageable(journeysService.findVehicleByNumberPlate(numberPlate), pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("journeysList",journeys.getContent());
        model.addAttribute("page", journeys);
        return "vehicle/vehicleJourney";
    }

    @RequestMapping("/journey/list/vehicle/{plateNumber}")
    public String getVehicleList(@PathVariable("plateNumber") String plateNumber, Model model, Pageable pageable){
        Page<Journey> journeys = journeysService.findByVehiclePageable(journeysService.findVehicleByNumberPlate(plateNumber), pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("journeysList",journeys.getContent());
        model.addAttribute("page", journeys);
        return "journey/listVehicle";
    }



    @RequestMapping("/journey/list/vehicle/update")
    public String updateList(@RequestParam("plateNumber") String numberPlate, Model model, Pageable pageable){
        model.addAttribute("journeysList", journeysService.findByVehiclePageable(journeysService.findVehicleByNumberPlate(numberPlate), pageable));
        return "vehicle/vehicleJourney :: journeysTable";
    }


}
