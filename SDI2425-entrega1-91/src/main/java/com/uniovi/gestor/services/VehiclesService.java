package com.uniovi.gestor.services;

import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.repositories.VehiclesRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehiclesService {
    private final VehiclesRepository vehiclesRepository;

    public VehiclesService(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    @PostConstruct
    public void init() {
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehiclesRepository.findAll().forEach(vehicles::add);
        return vehicles;
    }

    public Vehicle getVehicle(int id) {
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

    public void deleteVehicle(Vehicle vehicle) {
        vehiclesRepository.delete(vehicle);
    }

}
