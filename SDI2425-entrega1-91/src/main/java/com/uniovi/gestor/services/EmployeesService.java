package com.uniovi.gestor.services;

import com.uniovi.gestor.repositories.EmployeesRepository;
import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.uniovi.gestor.entities.*;

import java.util.Optional;

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
        return employeesRepository.findAll(pageable);
    }
    public Employee getEmployee(Long id) {
        Optional<Employee> employee = employeesRepository.findById(id);
        return employee.orElse(null);
    }

    public void addEmployee(Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employeesRepository.save(employee);
    }

    public void editEmployee(Employee employee) {
        employeesRepository.save(employee);
    }

    public Employee getEmployeeByDni(String dni) {
        return employeesRepository.findByDni(dni);
    }

    public void deleteEmployee(Long id) {
        employeesRepository.deleteById(id);
    }



}