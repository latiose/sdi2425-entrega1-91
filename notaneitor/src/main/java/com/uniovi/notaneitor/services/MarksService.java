package com.uniovi.notaneitor.services;

import com.uniovi.notaneitor.entities.Employee;
import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.repositories.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class MarksService {
    @Autowired
    private MarksRepository marksRepository;

    /* Inyección de dependencias basada en constructor (opción recomendada)*/
    private final HttpSession httpSession;
    @Autowired
    public MarksService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Mark getMark(Long id) {
//        Set<Mark> consultedList = (Set<Mark>) httpSession.getAttribute("consultedList");
//        if (consultedList == null) {
//            consultedList = new HashSet<>();
//        }
//        Mark mark = marksRepository.findById(id).isPresent() ? marksRepository.findById(id).get() : new Mark();
//        consultedList.add(mark);
//        httpSession.setAttribute("consultedList", consultedList);
//        return mark;
        Mark mark = marksRepository.findById(id).isPresent() ? marksRepository.findById(id).get() : new Mark();
        return mark;

    }

    public Page<Mark> getMarks(Pageable pageable) {
        Page<Mark> marks = marksRepository.findAll(pageable);
        return marks;
    }

   // public Mark getMark(Long id) {
      //  return marksRepository.findById(id).get();
   // }

    public void addMark(Mark mark) {
        marksRepository.save(mark);
    }

    public void deleteMark(Long id) {
        marksRepository.deleteById(id);

    }

    public void setMarkResend(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Mark mark = marksRepository.findById(id).get();
        if(mark.getUser().getDni().equals(dni) ) {
            marksRepository.updateResend(revised, id);
        }
    }

    public Page<Mark> getMarksForUser(Pageable pageable, Employee employee) {
        Page<Mark> marks = new PageImpl<Mark>(new LinkedList<>());
        if (employee.getRole().equals("ROLE_STUDENT")) {
            marks = marksRepository.findAllByUser(pageable, employee);}
        if (employee.getRole().equals("ROLE_PROFESSOR")) {
            marks = getMarks(pageable); }
        return marks;
    }

    public Page<Mark> searchMarksByDescriptionAndNameForUser(Pageable pageable,String searchText, Employee employee) {
        Page<Mark> marks = new PageImpl<Mark>(new LinkedList<>());
        searchText = "%"+searchText+"%";
        if (employee.getRole().equals("ROLE_STUDENT")) {
            marks = marksRepository.searchByDescriptionNameAndUser(pageable,searchText, employee);
        }
        if (employee.getRole().equals("ROLE_PROFESSOR")) {
            marks = marksRepository.searchByDescriptionAndName(pageable,searchText);
        }
        return marks;
    }


}