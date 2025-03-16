package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JourneysRepository extends CrudRepository<Journey, Long> {
    @SuppressWarnings("unused")
    @Query("SELECT j.vehicle FROM Journey j WHERE j.id = ?1")
    Vehicle findVehicleByJourneyId(Long id);

    @SuppressWarnings("unused")
    Page<Journey> findAll(Pageable pageable);

    @Query("SELECT j.vehicle FROM Journey j WHERE LOWER(j.vehicle.numberPlate) = LOWER(?1)")
    Vehicle findByNumberPlate(String numberPlate);


    Page<Journey> findByVehicle(Vehicle vehicle, Pageable pageable);
    List<Journey> findByVehicle(Vehicle vehicle);

    @SuppressWarnings("unused")
    @Query("SELECT j FROM Journey j WHERE LOWER(j.employee.dni) = LOWER(?1)")
    List<Journey> findByDni(String dni);

    @Query("SELECT j FROM Journey j WHERE LOWER(j.employee.dni) = LOWER(?1) ORDER BY CASE WHEN j.endDate IS NULL THEN 0 ELSE 1 END, j.startDate DESC")
    Page<Journey> findByDniPage(String dni, Pageable pageable); //para que el que está en curso sea el primero

    @Query("SELECT j FROM Journey j WHERE j.endDate=null and LOWER(j.employee.dni) = LOWER(?1)")
    Journey findActiveJourneyByDni(String dni);

    @Modifying
    @Transactional
    @Query("DELETE FROM Journey j WHERE j.vehicle = ?1")
    void deleteByVehicle(Vehicle vehicle);
}
