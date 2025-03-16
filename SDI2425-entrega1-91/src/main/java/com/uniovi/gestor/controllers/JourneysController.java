package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Journey;


import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.JourneysService;

import com.uniovi.gestor.services.LogService;
import com.uniovi.gestor.services.VehiclesService;
import com.uniovi.gestor.validators.AddJourneyFormValidator;
import com.uniovi.gestor.validators.EditJourneyFormValidator;
import com.uniovi.gestor.validators.EndJourneyFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class JourneysController {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;
    private final EmployeesService employeesService;
    private final EndJourneyFormValidator endJourneyFormValidator;
    private final AddJourneyFormValidator addJourneyFormValidator;
    private final EditJourneyFormValidator editJourneyFormValidator;
    private final LogService logService;


    public JourneysController(JourneysService journeysService, VehiclesService vehiclesService, EndJourneyFormValidator endJourneyFormValidator, EmployeesService employeeService, AddJourneyFormValidator addJourneyFormValidator, LogService logService,EditJourneyFormValidator editJourneyFormValidator) {
        this.journeysService = journeysService;
        this.vehiclesService = vehiclesService;
        this.employeesService = employeeService;
        this.endJourneyFormValidator = endJourneyFormValidator;
        this.addJourneyFormValidator = addJourneyFormValidator;
        this.logService = logService;
        this.editJourneyFormValidator = editJourneyFormValidator;
    }

    @RequestMapping(value = "/journey/add")
    public String getJourney(Model model){
        logService.log("PET", "PET [GET] /journey/add");
        model.addAttribute("journey", new Journey());
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        return "journey/add";
    }

    @RequestMapping(value = "/journey/end")
    public String getJourneyEnd(Model model){
        logService.log("PET", "PET [GET] /journey/end");
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
        logService.log("PET", "PET [POST] /journey/end | parameters: JOURNEY = " + journey.toString());
        endJourneyFormValidator.validate(journey, result);
        if (result.hasErrors()) {
            return "journey/end";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Journey existingJourney = journeysService.findActiveJourneyByDni(dni);

        if (existingJourney == null) {
            return "redirect:/journey/list";
        }

        Vehicle existingVehicle = existingJourney.getVehicle();
        if (existingVehicle == null) {
            return "redirect:/journey/list";
        }

        existingVehicle.setMileage(journey.getOdometerEnd());
        existingJourney.setOdometerEnd(journey.getOdometerEnd());
        existingJourney.setObservations(journey.getObservations());
        existingJourney.setEndDate(LocalDateTime.now());
        journeysService.addJourney(existingJourney);

        return "redirect:/journey/list/vehicle/" + existingVehicle.getNumberPlate();
    }



    @RequestMapping(value="/journey/add", method = RequestMethod.POST)
    public String addJourney(@RequestParam(value = "plateNumber", required = false) String plateNumber,
                             @ModelAttribute("journey") Journey journey,
                             BindingResult result, Model model) {
        logService.log("PET", "PET [POST] /journey/add | parameters: PLATE = "
                + plateNumber + ", JOURNEY = "
                + journey.toString());

        if (plateNumber == null || plateNumber.isEmpty()) {
            List<String> plates = vehiclesService.findAllPlates();
            if (!plates.isEmpty()) {
                plateNumber = plates.get(0);
            } else {
                result.rejectValue("vehicle.numberPlate", "Error.noVehicles", "No hay vehículos disponibles.");
                model.addAttribute("plateList", plates);
                return "journey/add";
            }
        }

        Vehicle vehicle = vehiclesService.findVehicleByNumberPlate(plateNumber);
        if (vehicle == null) {
            result.rejectValue("vehicle.numberPlate", "Error.invalidPlate", "Matrícula no válida.");
        } else {
            journey.setVehicle(vehicle);
        }

        addJourneyFormValidator.validate(journey, result);

        if (result.hasErrors()) {
            model.addAttribute("plateList", vehiclesService.findAllPlates());
            return "journey/add";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        journey.setOdometerStart(vehicle.getMileage());
        journey.setEmployee(employeesService.getEmployeeByDni(dni));
        journeysService.addJourney(journey);

        return "redirect:/journey/list/vehicle/" + plateNumber;
    }



    @RequestMapping("/journey/list")
    public String getJourneyList(Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /journey/list | parameters: PAGE = "
                + pageable.getPageNumber());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Page<Journey> journeys = journeysService.findByDniPage(dni,pageable);
        model.addAttribute("journeyList",journeys.getContent());
        model.addAttribute("page", journeys);
        return "journey/list";
    }

    @RequestMapping("/journey/list/update")
    public String updateList(Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /journey/list/update | parameters: PAGE = "
                + pageable.getPageNumber());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Page<Journey> journeys = journeysService.findByDniPage(dni,pageable);
        model.addAttribute("journeyList",journeys.getContent());
        return "journey/list :: journeyTable";
    }

    @RequestMapping("/journey/list/vehicle")
    public String getVehicleList(@RequestParam(value = "plateNumber", required = false) String plateNumber,
                                 Model model, Pageable pageable) {
        logService.log("PET", "PET [GET] /journey/list/vehicle | parameters: PAGE = "
                + pageable.getPageNumber() + ", PLATE = " + plateNumber);

        if (plateNumber == null || plateNumber.isEmpty()) {
            List<String> plates = vehiclesService.findAllPlates();
            if (!plates.isEmpty()) {
                plateNumber = plates.get(0);
            }
        }
        Page<Journey> journeys = journeysService.findByVehiclePageable(
                journeysService.findVehicleByNumberPlate(plateNumber), pageable);
        model.addAttribute("plateList", vehiclesService.findAllPlates());
        model.addAttribute("journeysList", journeys.getContent());
        model.addAttribute("page", journeys);
        model.addAttribute("selectedPlate", plateNumber);
        return "vehicle/vehicleJourney";
    }

    @RequestMapping("/journey/list/vehicle/{plateNumber}")
    public String getSingleVehicleList(@PathVariable("plateNumber") String plateNumber, Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /journey/list/vehicle/"
                + plateNumber + " | parameters: PLATE = "
                + plateNumber
                + ", PAGE = " + pageable.getPageNumber());
        Page<Journey> journeys = journeysService.findByVehiclePageable(journeysService.findVehicleByNumberPlate(plateNumber), pageable);
        model.addAttribute("plateList",vehiclesService.findAllPlates());
        model.addAttribute("journeysList",journeys.getContent());
        model.addAttribute("page", journeys);
        return "journey/listVehicle";
    }



    @RequestMapping("/journey/list/vehicle/update")
    public String updateList(@RequestParam("plateNumber") String numberPlate, Model model, Pageable pageable){
        logService.log("PET", "PET [GET] /journey/list/vehicle/update | parameters: PLATE = "
                + numberPlate
                + ", PAGE = " + pageable.getPageNumber());
        model.addAttribute("journeysList", journeysService.findByVehiclePageable(journeysService.findVehicleByNumberPlate(numberPlate), pageable));
        return "vehicle/vehicleJourney :: journeysTable";
    }


    @RequestMapping(value = "/journey/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        logService.log("PET", "PET [GET] /journey/edit/" + id + " | parameters: ID = " + id);
        Journey journey = journeysService.getJourney(id);
        model.addAttribute("journey", journey);
        return "journey/edit";
    }

    @RequestMapping(value = "/journey/edit/{id}", method = RequestMethod.POST)
    public String setEdit(Journey journey, BindingResult result, @PathVariable Long id, Model model) {
        logService.log("PET", "PET [POST] /journey/edit/" + id + " | parameters: ID = " + id + ", JOURNEY = " + journey.toString());
        Journey originalJourney = journeysService.getJourney(id);
        journey.setVehicle(originalJourney.getVehicle());
        journey.setEmployee(originalJourney.getEmployee());
        journey.setId(id);
        editJourneyFormValidator.validate(journey, result);

        if (result.hasErrors()) {
            return "journey/edit";
        }

        Journey updatedJourney = journeysService.getJourney(id);
        updatedJourney.setOdometerEnd(journey.getOdometerEnd());
        updatedJourney.setOdometerStart(journey.getOdometerStart());
        updatedJourney.setEndDate(journey.getEndDate());
        updatedJourney.setStartDate(journey.getStartDate());
        journeysService.addJourney(updatedJourney);

        return "redirect:/journey/list/vehicle/" + updatedJourney.getVehicle().getNumberPlate();
    }



}
