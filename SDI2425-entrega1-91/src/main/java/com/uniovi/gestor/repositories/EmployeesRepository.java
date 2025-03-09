package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeesRepository extends CrudRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE UPPER(e.dni) = UPPER(?1)")
    Employee findByDni(String dni);

    Page<Employee> findAll(Pageable pageable);
}
