package com.uniovi.gestor.services;
import java.util.*;

import com.uniovi.gestor.repositories.EmployeesRepository;
import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.uniovi.gestor.entities.*;

@Service
public class EmployeesService {
    private final EmployeesRepository employeesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public EmployeesService(EmployeesRepository employeesRepository, BCryptPasswordEncoder
            bCryptPasswordEncoder) {
        this.employeesRepository = employeesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @PostConstruct
    public void init() {
    }
    public Page<Employee> getEmployees(Pageable pageable) {
        Page<Employee> employees = employeesRepository.findAll(pageable);
        return employees;
    }
    public Employee getEmployee(Long id) {
        return employeesRepository.findById(id).get();
    }

    public void addEmployee(Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employeesRepository.save(employee);
    }
    public Employee getEmployeeByDni(String dni) {
        return employeesRepository.findByDni(dni);
    }

    public void deleteEmployee(Long id) {
        employeesRepository.deleteById(id);
    }



}