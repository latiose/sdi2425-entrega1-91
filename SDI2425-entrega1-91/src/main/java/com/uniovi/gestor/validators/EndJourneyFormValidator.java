package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.services.JourneysService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EndJourneyFormValidator implements Validator {

    private final JourneysService journeysService;

    public EndJourneyFormValidator(JourneysService journeysService) {
        this.journeysService = journeysService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Journey.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Journey journey = (Journey) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "odometerEnd", "Error.empty");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Journey journeyActivo = journeysService.findActiveJourneyByDni(dni);
        if(journeyActivo == null) {
            errors.reject("id");
            return;
        }
        if (journey.getOdometerEnd() < 0)
            errors.rejectValue("odometerEnd", "Error.odometer.negativo");
        if (journey.getOdometerEnd() < journeyActivo.getOdometerStart())
            errors.rejectValue("odometerEnd", "Error.odometer.menor");


    }
}
