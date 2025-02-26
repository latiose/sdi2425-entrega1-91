package com.uniovi.gestor.controllers;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Mark;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.MarksService;
import com.uniovi.gestor.validators.AddMarkFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class MarksController {
    private final MarksService marksService;
    private final EmployeesService employeesService;
    private final AddMarkFormValidator validator;
    private final HttpSession httpSession;
    public MarksController(MarksService marksService, EmployeesService employeesService, AddMarkFormValidator validator, HttpSession httpSession) {
        this.marksService = marksService;
        this.employeesService = employeesService;
        this.validator = validator;
        this.httpSession = httpSession;
    }

    @RequestMapping("/mark/list")
    public String getList(Model model, Pageable pageable, Principal principal,
                          @RequestParam(value = "", required = false) String searchText) {
        Page<Mark> marks;
        String dni = principal.getName(); // DNI es el name de la autenticación
        Employee employee = employeesService.getEmployeeByDni(dni);

        if (searchText != null && !searchText.isEmpty()) {
            marks = marksService.searchMarksByDescriptionAndNameForUser(pageable, searchText, employee);
        } else {
          marks = marksService.getMarksForUser(pageable, employee);
        }
        model.addAttribute("marksList", marks.getContent());
        model.addAttribute("page", marks);
        return "mark/list";
    }


//    @RequestMapping(value = "/mark/add", method = RequestMethod.POST)
//    public String setMark(@RequestParam String description, @RequestParam String score) {
//        return "Adding "+description+" with score: "+score;
//    }

//    @RequestMapping("/mark/details")
//    public String getDetail(@RequestParam Long id) {
//        return "Getting Details =>" + id;
//    }

    @RequestMapping(value="/mark/add", method = RequestMethod.GET)
    public String getMark(Model model){
        model.addAttribute("mark", new Mark());
       // model.addAttribute("usersList", employeesService.getUsers());
        return "mark/add";
    }

    @RequestMapping(value = "/mark/add", method = RequestMethod.POST)
    public String setMark(@Validated Mark mark, BindingResult result) {

        validator.validate(mark, result);
        if (result.hasErrors()) {
            return "mark/add";
        }
        marksService.addMark(mark);

        return "redirect:/mark/list";
    }


    @RequestMapping(value = "/mark/edit/{id}", method = RequestMethod.POST)
    public String setEdit(@Validated Mark mark, BindingResult result, @PathVariable Long id) {
        validator.validate(mark, result);
        if (result.hasErrors()) {
            String resultado = "mark/edit";
            return resultado;
        }
        Mark originalMark = marksService.getMark(id);
        // modificar solo score y description
        originalMark.setScore(mark.getScore());
        originalMark.setDescription(mark.getDescription());
        marksService.addMark(originalMark);
        return "redirect:/mark/details/" + id;
    }
    @RequestMapping("/mark/details/{id}")
    public String getDetail(Model model, @PathVariable Long id) {
        model.addAttribute("mark", marksService.getMark(id));
        return "mark/details";
    }


    @RequestMapping("/mark/delete/{id}")
    public String deleteMark(@PathVariable Long id) {
        marksService.deleteMark(id);
        return "redirect:/mark/list";

    }

    @RequestMapping(value = "/mark/edit/{id}",method = RequestMethod.GET)
    public String getEdit(Model model, @PathVariable Long id) {
        model.addAttribute("mark", marksService.getMark(id));
        //model.addAttribute("usersList", employeesService.getUsers());
        return "mark/edit";
    }


    @RequestMapping("/mark/list/update")
    public String updateList(Model model, Pageable pageable,Principal principal) {
        String dni = principal.getName(); // DNI es el name de la autenticación
        Employee employee = employeesService.getEmployeeByDni(dni);
        Page<Mark> marks = marksService.getMarksForUser(pageable, employee);
        model.addAttribute("marksList", marks.getContent());
        return "mark/list :: tableMarks";
    }



    @RequestMapping(value = "/mark/{id}/resend", method = RequestMethod.GET)
    public String setResendTrue(@PathVariable Long id) {
        marksService.setMarkResend(true, id);
        return "redirect:/mark/list";
    }
    @RequestMapping(value = "/mark/{id}/noresend", method = RequestMethod.GET)
    public String setResendFalse(@PathVariable Long id) {
        marksService.setMarkResend(false, id);
        return "redirect:/mark/list";
    }

}
