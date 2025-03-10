package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface JourneysRepository extends CrudRepository<Journey, Long> {

    Page<Journey> findAll(Pageable pageable);
}
