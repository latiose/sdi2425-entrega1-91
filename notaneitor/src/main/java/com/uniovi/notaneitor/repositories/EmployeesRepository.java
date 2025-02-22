package com.uniovi.notaneitor.repositories;

import com.uniovi.notaneitor.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeesRepository extends CrudRepository<Employee, Long> {
    Employee findByDni(String dni);
}
