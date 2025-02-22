package com.uniovi.notaneitor.validators;

import com.uniovi.notaneitor.entities.Employee;
import com.uniovi.notaneitor.services.EmployeesService;
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
        char lastChar = employee.getDni().charAt(employee.getDni().length() - 1);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        if (employee.getDni().length() != 9 || Character.isDigit(lastChar)) {
            errors.rejectValue("DNI", "Error.professor");
        }

        if (employeesService.getEmployeeByDni(employee.getDni()) != null) {
            errors.rejectValue("DNI", "Error.professor");
        }


    }
}


