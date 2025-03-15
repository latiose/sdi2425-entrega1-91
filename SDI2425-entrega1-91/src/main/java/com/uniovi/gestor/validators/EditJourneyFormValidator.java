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
public class EditJourneyFormValidator implements Validator {

    private final JourneysService journeysService;
    private final VehiclesService vehiclesService;

    public EditJourneyFormValidator(JourneysService journeysService, VehiclesService vehiclesService) {
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
        if(journey.getEndDate() == null ) {
            errors.rejectValue("endDate", "Error.empty");
            return;
        }
        if(journey.getStartDate() == null) {
            errors.rejectValue("startDate", "Error.empty");
            return;
        }

        String plateNumber = journey.getVehicle().getNumberPlate();

        Vehicle vehicle = vehiclesService.findVehicleByNumberPlate(plateNumber);

        List<Journey> vehicleJourneys = journeysService.findByVehicle(vehicle);
        for (Journey j : vehicleJourneys) {
            if (j.getEndDate() == null) {
                errors.rejectValue("journey", "Error.vehicleInUse");
            }
        }

        if (journey.getOdometerEnd() < 0 || journey.getOdometerStart() < 0)
            errors.rejectValue("odometerEnd", "Error.odometer.negativo");
        if (journey.getOdometerEnd() < journey.getOdometerStart())
            errors.rejectValue("odometerEnd", "Error.odometer.menor");
        if(journey.getEndDate().isBefore(journey.getStartDate()))
            errors.rejectValue("endDate", "Error.dateAfter");
    }
}

