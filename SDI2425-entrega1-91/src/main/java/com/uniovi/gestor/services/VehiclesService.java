package com.uniovi.gestor.services;

import com.uniovi.gestor.VehicleStatusConfig;
import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.repositories.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehiclesService {
    @Autowired
    private VehicleStatusConfig statusConfig;
    private final VehiclesRepository vehiclesRepository;

    public VehiclesService(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    @PostConstruct
    public void init() {
    }

    public Page<Vehicle> getVehicles(Pageable pageable) {
        return vehiclesRepository.findAll(pageable);
    }

    public Vehicle getVehicle(Long id) {
        return vehiclesRepository.findById(id).get();
    }

    public void addVehicle(Vehicle vehicle) {
        vehiclesRepository.save(vehicle);
    }

    public Vehicle getVehicleByNumberPlate(String plate) {
        return vehiclesRepository.findByNumberPlate(plate);
    }

    public Vehicle getVehicleByVin(String vin) {
        return vehiclesRepository.findByVin(vin);
    }

    public void deleteVehicle(Long id) {
        vehiclesRepository.deleteById(id);
    }

    public List<String> findAllPlates(){
        return vehiclesRepository.findAllPlates();
    }

    public Vehicle findVehicleByNumberPlate(String plate) {
    return getVehicleByNumberPlate(plate);
    }

    public String getDisplayStatus(Vehicle vehicle) {
        return statusConfig.getStatusDisplay(vehicle.getStatus());
    }

   

}
