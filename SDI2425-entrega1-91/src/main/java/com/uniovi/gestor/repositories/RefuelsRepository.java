package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefuelsRepository extends CrudRepository<Refuel, Long> {

    Page<Refuel> findAll(Pageable pageable);

    @Query("SELECT r FROM Refuel r WHERE LOWER(r.journey.vehicle.numberPlate) = LOWER(?1)")
    Page<Refuel> findByNumberPlate(String numberPlate, Pageable pageable);
}
