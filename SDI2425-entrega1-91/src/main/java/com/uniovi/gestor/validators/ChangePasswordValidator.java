package com.uniovi.gestor.validators;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.regex.Pattern;

@Component
public class ChangePasswordValidator implements Validator {

    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$"
    );

    public ChangePasswordValidator() {
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmNewPassword", "Error.empty");

        Employee actualEmployee = employeesService.getEmployeeByDni(employee.getDni());
        if(!bCryptPasswordEncoder.matches(employee.getPassword(), actualEmployee.getPassword())) {
            errors.rejectValue("password", "Error.password.incorrect");
        }

        if(!PASSWORD_PATTERN.matcher(employee.getNewPassword()).matches()) {
            errors.rejectValue("newPassword", "Error.password.weak");
        }

        if(!employee.getNewPassword().equals(employee.getConfirmNewPassword())){
            errors.rejectValue("passwordConfirm", "Error.password.passwordConfirm");
        }

    }
}


