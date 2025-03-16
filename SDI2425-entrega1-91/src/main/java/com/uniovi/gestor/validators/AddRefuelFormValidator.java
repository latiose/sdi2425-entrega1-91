package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.services.RefuelsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddRefuelFormValidator implements Validator {
    private final RefuelsService refuelsService;

    public AddRefuelFormValidator(RefuelsService refuelsService) {
        this.refuelsService = refuelsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refuel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refuel refuel = (Refuel) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "station", "Error.empty");
        if(refuel.getStation() != null && !refuel.getStation().trim().equals(refuel.getStation())){
            errors.rejectValue("station", "Error.space");
        }
        if(refuel.getPrice() <= 0){
            errors.rejectValue("price", "Error.negative");
        }
        if(refuel.getAmount() <= 0){
            errors.rejectValue("amount", "Error.negative");
        }
        Journey activeJourney = refuelsService.getActiveJourney();

        if(activeJourney == null){
            errors.reject("Error.journeyNotStarted");
        }
        if(activeJourney != null){
            double odometerStart = activeJourney.getOdometerStart();
            if(refuel.getOdometer() <= odometerStart)
                errors.rejectValue("odometer", "Error.odometer");
        }

    }
}
