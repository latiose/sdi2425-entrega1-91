package com.uniovi.gestor.repositories;

import com.uniovi.gestor.VehicleStatusConfig;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehiclesRepository extends CrudRepository<Vehicle, Long> {
    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.numberPlate) = LOWER(?1)")
    Vehicle findByNumberPlate(String numberPlate);

    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.vin) = LOWER(?1)")
    Vehicle findByVin(String vin);

    Page<Vehicle> findByStatus(VehicleStatusConfig.VehicleStatus status, Pageable pageable);

    Page<Vehicle> findAll(Pageable pageable);

    @Query("SELECT v.numberPlate FROM Vehicle v ORDER BY v.numberPlate ASC")
    List<String> findAllPlates();


    @Query("SELECT v FROM Vehicle v WHERE v.status = 'AVAILABLE'")
    List<Vehicle> findAvailableVehicles();

}
