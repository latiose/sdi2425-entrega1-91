package com.uniovi.notaneitor.servicies;


import com.uniovi.notaneitor.entities.Category;
import com.uniovi.notaneitor.entities.Professor;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProfessorsService {
    private final List<Professor> professorList =new LinkedList<Professor>();

   @PostConstruct
    public void init() {
       professorList.add(new Professor(1L, Category.CATEDRATICO,"Perez","Pablo","123A"));
       professorList.add(new Professor(2L, Category.CATEDRATICO,"Alvarez","Pablo","321A"));
    }

    public List<Professor> getProfessors() {
        return professorList;
    }

    public Professor getProfessor(Long id) {
        return professorList.stream() .filter(professor -> professor.getId().equals(id)).findFirst().get();
    }

    public void addProfessor(Professor professor) {
        professorList.add(professor);
    }

    public void deleteProfessor(Long id) {
        professorList.remove(getProfessor(id));
    }
}