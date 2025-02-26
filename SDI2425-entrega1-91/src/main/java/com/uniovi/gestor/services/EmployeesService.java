package com.uniovi.gestor.services;
import java.util.*;

import com.uniovi.gestor.repositories.EmployeesRepository;
import javax.annotation.PostConstruct;

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
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        employeesRepository.findAll().forEach(employees::add);
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