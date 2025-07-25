package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.JourneysService;
import com.uniovi.gestor.services.VehiclesService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import org.springframework.validation.Validator;

import java.util.List;


@Component
public class AddJourneyFormValidator implements Validator {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;

    public AddJourneyFormValidator(JourneysService journeysService, VehiclesService vehiclesService) {
        this.journeysService = journeysService;
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Journey.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Journey journey = (Journey) target;

        String plateNumber = journey.getVehicle().getNumberPlate();

        Vehicle vehicle = vehiclesService.findVehicleByNumberPlate(plateNumber);
        if (vehicle == null) {
            errors.rejectValue("vehicle.numberPlate", "Error.empty");
            return;
        }

        List<Journey> vehicleJourneys = journeysService.findByVehicle(vehicle);
        for (Journey j : vehicleJourneys) {
            if (j.getEndDate() == null) {
                errors.rejectValue("vehicle.numberPlate", "Error.vehicleInUse");
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Journey userJourneys = journeysService.findActiveJourneyByDni(dni);
        if(userJourneys != null ) errors.rejectValue("vehicle.numberPlate", "Error.journeyStarted");

        }
    }


