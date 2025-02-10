package com.uniovi.notaneitor.services;

import com.uniovi.notaneitor.entities.Mark;
import com.uniovi.notaneitor.repositories.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarksService {
    @Autowired
    private MarksRepository marksRepository;
    //private final List<Mark> marksList =new LinkedList<>();

//    @PostConstruct
//    public void init() {
//        marksList.add(new Mark(1L, "Ejercicio 1", 10.0));
//        marksList.add(new Mark(2L, "Ejercicio 2", 9.0));
//    }

    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<Mark>();
        marksRepository.findAll().forEach(marks::add);
        return marks;
    }

    public Mark getMark(Long id) {
        return marksRepository.findById(id).get();
    }

    public void addMark(Mark mark) {
        marksRepository.save(mark);
    }

    public void deleteMark(Long id) {
        marksRepository.deleteById(id);

    }
}