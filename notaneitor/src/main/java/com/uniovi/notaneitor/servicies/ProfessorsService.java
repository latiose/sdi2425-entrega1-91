package com.uniovi.notaneitor.servicies;


import com.uniovi.notaneitor.entities.Category;
import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.entities.Professor;
import com.uniovi.notaneitor.repositories.MarksRepository;
import com.uniovi.notaneitor.repositories.ProfessorsRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProfessorsService {
    @Autowired
    private ProfessorsRepository professorsRepository;
   // private final List<Professor> professorList =new LinkedList<Professor>();

  /* @PostConstruct
    public void init() {
       professorList.add(new Professor(1L, "Catedratico","Perez","Pablo","123A"));
       professorList.add(new Professor(2L, "Catedratico","Alvarez","Pablo","321A"));
    }
*/
    public List<Professor> getProfessors() {
        List<Professor> professors = new ArrayList<>();
        professorsRepository.findAll().forEach(professors::add);
        return professors;
    }

    public Professor getProfessor(Long id) {
        return professorsRepository.findById(id).get();
    }

    public void addProfessor(Professor professor) {
        professorsRepository.save(professor);

    }

    public void deleteProfessor(Long id) {
        professorsRepository.deleteById(id);
    }
}