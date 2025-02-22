package com.uniovi.notaneitor.validators;
import com.uniovi.notaneitor.entities.Employee;
import com.uniovi.notaneitor.services.EmployeesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
@Component
public class SignUpFormValidator implements Validator {
    private final EmployeesService employeesService;
    public SignUpFormValidator(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");
        if (employee.getDni().length() < 5 || employee.getDni().length() > 24) {
            errors.rejectValue("dni", "Error.signup.dni.length");}

        if (employeesService.getEmployeeByDni(employee.getDni()) != null) {
            errors.rejectValue("dni", "Error.signup.dni.duplicate");}
        if (employee.getName().length() < 5 || employee.getName().length() > 24) {
            errors.rejectValue("name", "Error.signup.name.length");}
        if (employee.getLastName().length() < 5 || employee.getLastName().length() > 24) {
            errors.rejectValue("lastName", "Error.signup.lastName.length");}
        if (employee.getPassword().length() < 5 || employee.getPassword().length() > 24) {
            errors.rejectValue("password", "Error.signup.password.length");}
        if (!employee.getPasswordConfirm().equals(employee.getPassword())) {
            errors.rejectValue("passwordConfirm",
                    "Error.signup.passwordConfirm.coincidence");}
    }
}
