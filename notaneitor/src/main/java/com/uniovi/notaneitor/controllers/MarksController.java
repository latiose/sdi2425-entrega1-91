package com.uniovi.notaneitor.controllers;

import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.entities.User;
import com.uniovi.notaneitor.services.MarksService;
import com.uniovi.notaneitor.services.UsersService;
import com.uniovi.notaneitor.validators.AddMarkFormValidator;
import com.uniovi.notaneitor.validators.AddProfessorFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class MarksController {
    private final MarksService marksService;
    private final UsersService usersService;
    private final AddMarkFormValidator validator;
    private final HttpSession httpSession;
    public MarksController(MarksService marksService, UsersService usersService, AddMarkFormValidator validator, HttpSession httpSession) {
        this.marksService = marksService;
        this.usersService = usersService;
        this.validator = validator;
        this.httpSession = httpSession;
    }

    @RequestMapping("/mark/list")
    public String getList(Model model) {

        model.addAttribute("markList", marksService.getMarks());
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
        model.addAttribute("usersList", usersService.getUsers());
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
        model.addAttribute("usersList", usersService.getUsers());
        return "mark/edit";
    }


    @RequestMapping("/mark/list/update")
    public String updateList(Model model){
        model.addAttribute("markList", marksService.getMarks() );
        return "mark/list :: marksTable";
    }

}
