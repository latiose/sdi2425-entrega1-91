package com.uniovi.notaneitor.controllers;

import com.uniovi.notaneitor.entities.Professor;
import com.uniovi.notaneitor.entities.User;
import com.uniovi.notaneitor.services.MarksService;
import com.uniovi.notaneitor.services.ProfessorsService;
import com.uniovi.notaneitor.services.UsersService;
import com.uniovi.notaneitor.validators.AddMarkFormValidator;
import com.uniovi.notaneitor.validators.AddProfessorFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class ProfessorsController {

    private final ProfessorsService professorsService;
    private final AddProfessorFormValidator validator;

    public ProfessorsController(ProfessorsService professorsService, AddProfessorFormValidator validator) {
        this.professorsService = professorsService;
        this.validator = validator;
    }

    @RequestMapping("/professor/list")
    public String getList(Model model) {
        model.addAttribute("professorsList", professorsService.getProfessors());
        //List<Professor> professorList =new LinkedList<Professor>();
        //professorList = professorsService.getProfessors();
        //String result = "";
        //for(Professor p: professorList){
          //  result+=p.toString()+"\n";
        //}
        return "professor/list";
    }


    @RequestMapping(value = "/professor/add", method = RequestMethod.POST)
    public String setProfessor(@Validated Professor professor, BindingResult result) {
        validator.validate(professor, result);
        if (result.hasErrors()) {
            return "professor/add";
        }
        professorsService.addProfessor(professor);
        return "redirect:/professor/list";
    }

    @RequestMapping(value = "/professor/add", method = RequestMethod.GET)
    public String getProfessor(Model model) {
        model.addAttribute("professor", new Professor());
        return "professor/add";
    }

    @RequestMapping(value="/professor/edit/{id}", method=RequestMethod.POST)
    public String setEdit(@Validated Professor professor, BindingResult result, @PathVariable Long id){
        validator.validate(professor, result);
        if (result.hasErrors()) {
            return "professor/edit";
        }
        professor.setId(id);
        professorsService.addProfessor(professor);
        return "redirect:/professor/details/"+id;
    }

    @RequestMapping("/professor/details/{id}")
    public String getDetail(Model model, @PathVariable Long id) {
        model.addAttribute("professor", professorsService.getProfessor(id));
        return "professor/details";
    }


    @RequestMapping("/professor/delete/{id}")
    public String deleteMark(@PathVariable Long id) {
        professorsService.deleteProfessor(id);
        return "redirect:/professor/list";

    }

    @RequestMapping(value = "/professor/edit/{id}", method = RequestMethod.GET)
    public String getEdit(Model model, @PathVariable Long id) {
        model.addAttribute("professor", professorsService.getProfessor(id));
        return "professor/edit";
    }

}
