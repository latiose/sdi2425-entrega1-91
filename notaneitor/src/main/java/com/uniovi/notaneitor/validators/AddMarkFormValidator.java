package com.uniovi.notaneitor.validators;

import com.uniovi.notaneitor.entities.Employee;
import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.services.MarksService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddMarkFormValidator implements Validator {
    private final MarksService marksService;
    public AddMarkFormValidator(MarksService marksService) {
        this.marksService = marksService;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Mark mark = (Mark) target;

        if (mark.getScore() < 0 || mark.getScore() > 10) {
            errors.rejectValue("score", "Error.mark");}
        if (mark.getDescription().length() < 20) {
            errors.rejectValue("description", "Error.mark");}

    }
}
