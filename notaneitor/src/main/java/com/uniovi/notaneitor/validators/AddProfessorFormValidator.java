package com.uniovi.notaneitor.validators;

import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.entities.Professor;
import com.uniovi.notaneitor.entities.User;
import com.uniovi.notaneitor.services.MarksService;
import com.uniovi.notaneitor.services.ProfessorsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddProfessorFormValidator implements Validator {
    private final ProfessorsService professorsService;

    public AddProfessorFormValidator(ProfessorsService professorsService) {
        this.professorsService = professorsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Professor professor = (Professor) target;
        char lastChar = professor.getDNI().charAt(professor.getDNI().length() - 1);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        if (professor.getDNI().length() != 9 || Character.isDigit(lastChar)) {
            errors.rejectValue("DNI", "Error.professor");
        }

        if (professorsService.getProfessorByDni(professor.getDNI()) != null) {
            errors.rejectValue("DNI", "Error.professor");
        }


    }
}


