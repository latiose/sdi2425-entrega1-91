package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.services.EmployeesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddEmployeeFormValidator implements Validator {
    private final EmployeesService employeesService;

    public AddEmployeeFormValidator(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");
        if (employee.getLastName() != null && !employee.getLastName().trim().equals(employee.getLastName())) {
            errors.rejectValue("lastName", "Error.space");
        }
        if (employee.getName() != null && !employee.getName().trim().equals(employee.getName())) {
            errors.rejectValue("name", "Error.space");
        }

        if(employee.getDni().length() != 9){
            errors.rejectValue("dni", "Error.signup.dni.invalid");
            return;
        }

        String numberPart = employee.getDni().substring(0, 8);
        for (char c : numberPart.toCharArray()) {
            if (!Character.isDigit(c)) {
                errors.rejectValue("dni", "Error.signup.dni.invalid");
                return;
            }
        }
        char lastChar = employee.getDni().charAt(employee.getDni().length() - 1);
        int number = Integer.parseInt(numberPart);

        String letters = "TRWAGMYFPDXBNJZSQVHLCKE";

        if (Character.isDigit(lastChar) || lastChar != letters.charAt(number % 23)) {
            errors.rejectValue("dni", "Error.signup.dni.invalid");
        }

        if (employeesService.getEmployeeByDni(employee.getDni()) != null) {
            errors.rejectValue("dni", "Error.dni.duplicate");
        }


    }
}


