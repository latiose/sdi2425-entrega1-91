package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.services.VehiclesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class AddVehicleFormValidator implements Validator {

    private final VehiclesService vehiclesService;

    public AddVehicleFormValidator(VehiclesService vehiclesService) {
        this.vehiclesService = vehiclesService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Vehicle.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vehicle vehicle = (Vehicle) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberPlate", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "vin", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brand", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", "Error.empty");

        if (vehicle.getFuel() == null)
            errors.rejectValue("fuel", "Error.empty");

        if(vehicle.getNumberPlate() != null && !vehicle.getNumberPlate().trim().equals(vehicle.getNumberPlate()))
            errors.rejectValue("numberPlate", "Error.space");

        if(vehicle.getVin() != null && !vehicle.getVin().trim().equals(vehicle.getVin()))
            errors.rejectValue("vin", "Error.space");

        if(vehicle.getBrand() != null && !vehicle.getBrand().trim().equals(vehicle.getBrand()))
            errors.rejectValue("brand", "Error.space");

        if(vehicle.getModel() != null && !vehicle.getModel().trim().equals(vehicle.getModel()))
            errors.rejectValue("model", "Error.space");

        String numberPlatePattern = "^([0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}|[A-Z][0-9]{4}[A-Z]{2})$";
        if(!Pattern.matches(numberPlatePattern, vehicle.getNumberPlate()))
            errors.rejectValue("numberPlate", "Error.addvehicle.plate.invalid");

        if (vehiclesService.getVehicleByNumberPlate(vehicle.getNumberPlate()) != null) {
            errors.rejectValue("numberPlate", "Error.plate.duplicate");
        }

        if (vehiclesService.getVehicleByVin(vehicle.getVin()) != null) {
            errors.rejectValue("vin", "Error.vin.duplicate");
        }

        if (vehicle.getVin().length() != 17) {
            errors.rejectValue("vin", "Error.vin.length");
        }

    }
}
