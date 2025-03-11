package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JourneysRepository extends CrudRepository<Journey, Long> {

    Page<Journey> findAll(Pageable pageable);

    @Query("SELECT j.vehicle FROM Journey j WHERE LOWER(j.vehicle.numberPlate) = LOWER(?1)")
    Vehicle findByNumberPlate(String numberPlate);

    List<Journey> findByVehicle(Vehicle vehicle);

    @Query("SELECT j FROM Journey j WHERE LOWER(j.employee.dni) = LOWER(?1)")
    List<Journey> findByDni(String dni);

    @Query("SELECT j FROM Journey j WHERE LOWER(j.employee.dni) = LOWER(?1) and j.endDate is not null")
    Page<Journey> findFinishedForCurrentUser(String dni, Pageable pageable);

    @Query("SELECT j FROM Journey j WHERE j.endDate=null")
    Journey findActiveJourneyByDni(String dni);
}
