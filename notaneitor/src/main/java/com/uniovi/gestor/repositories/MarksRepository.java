package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface MarksRepository  extends CrudRepository<Mark, Long> {
   @Query("SELECT r FROM Mark r WHERE r.employee = ?1 ORDER BY r.id ASC")
    Page<Mark> findAllByUser(Pageable pageable, Employee employee);

    @Modifying
    @Transactional
    @Query("UPDATE Mark SET resend = ?1 WHERE id = ?2")
    void updateResend(Boolean resend, Long id);

    @Query("SELECT r FROM Mark r WHERE (LOWER(r.description) LIKE LOWER(?1) OR LOWER(r.employee.name) LIKE LOWER(?1))")
    Page<Mark> searchByDescriptionAndName(Pageable pageable,String searchText);

    @Query("SELECT r FROM Mark r WHERE (LOWER(r.description) LIKE LOWER(?1) OR LOWER(r.employee.name) LIKE LOWER(?1)) AND r.employee = ?2 ")
    Page<Mark> searchByDescriptionNameAndUser(Pageable pageable,String searchText, Employee employee);

    Page<Mark> findAll(Pageable pageable);


}
