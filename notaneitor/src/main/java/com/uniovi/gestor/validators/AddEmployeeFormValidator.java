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

        char lastChar = employee.getDni().charAt(employee.getDni().length() - 1);
        if (employee.getDni().length() != 9 || Character.isDigit(lastChar)) {
            errors.rejectValue("dni", "Error.signup.dni.invalid");
        }

        if (employeesService.getEmployeeByDni(employee.getDni()) != null) {
            errors.rejectValue("dni", "Error.dni.duplicate");
        }


    }
}


