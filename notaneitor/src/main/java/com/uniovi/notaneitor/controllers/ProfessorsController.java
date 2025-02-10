package com.uniovi.notaneitor.controllers;

import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.entities.Professor;
import com.uniovi.notaneitor.servicies.MarksService;
import com.uniovi.notaneitor.servicies.ProfessorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


@Controller
public class ProfessorsController {

    @Autowired //Inyectar el servicio
    private ProfessorsService professorsService;

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
    public String setProfessor(@ModelAttribute Professor professor) {
        professorsService.addProfessor(professor);
        return "redirect:/professor/list";
    }

    @RequestMapping(value = "/professor/add")
    public String getProfessor() {
        return "professor/add";
    }

    @RequestMapping(value="/professor/edit/{id}", method=RequestMethod.POST)
    public String setEdit(@ModelAttribute Professor professor, @PathVariable Long id){
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

    @RequestMapping(value = "/professor/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        model.addAttribute("professor", professorsService.getProfessor(id));
        return "professor/edit";
    }

}
