package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.services.EmployeesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EditEmployeeFormValidator implements Validator {

    public EditEmployeeFormValidator() {
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
        if (employee.getLastName() != null && !employee.getLastName().trim().equals(employee.getLastName())) {
            errors.rejectValue("lastName", "Error.space");
        }
        if (employee.getName() != null && !employee.getName().trim().equals(employee.getName())) {
            errors.rejectValue("name", "Error.space");
        }

    }
}


