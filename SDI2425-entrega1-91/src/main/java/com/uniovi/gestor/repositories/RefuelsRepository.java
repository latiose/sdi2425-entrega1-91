package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Refuel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RefuelsRepository extends CrudRepository<Refuel, Long> {
    @SuppressWarnings("unused")
    Page<Refuel> findAll(Pageable pageable);

    @Query("SELECT r FROM Refuel r WHERE LOWER(r.journey.vehicle.numberPlate) = LOWER(?1)")
    Page<Refuel> findByNumberPlate(String numberPlate, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Refuel r WHERE r.journey = ?1")
    void deleteByJourney(Journey journey);
}
