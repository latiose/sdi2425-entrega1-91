package com.uniovi.gestor.services;

import com.uniovi.gestor.VehicleStatusConfig;
import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.repositories.JourneysRepository;
import com.uniovi.gestor.repositories.RefuelsRepository;
import com.uniovi.gestor.repositories.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class VehiclesService {
    @Autowired
    private VehicleStatusConfig statusConfig;
    private final VehiclesRepository vehiclesRepository;
    private final JourneysRepository journeysRepository;
    private final RefuelsRepository refuelsRepository;


    public VehiclesService(VehiclesRepository vehiclesRepository, JourneysRepository journeysRepository, RefuelsRepository refuelsRepository) {
        this.vehiclesRepository = vehiclesRepository;
        this.journeysRepository = journeysRepository;
        this.refuelsRepository = refuelsRepository;
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

    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehiclesRepository.findById(id).get();
        List<Journey> journeys = journeysRepository.findByVehicle(vehicle);
        journeys.forEach(refuelsRepository::deleteByJourney);
        journeysRepository.deleteByVehicle(vehicle);
        vehiclesRepository.deleteById(id);
    }

    public List<String> findAllPlates(){
        return vehiclesRepository.findAllPlates();
    }

    public Vehicle findVehicleByNumberPlate(String plate) {
    return getVehicleByNumberPlate(plate);
    }

    public Page<Vehicle> findVehiclesByStatus(VehicleStatusConfig.VehicleStatus status, Pageable pageable) { return vehiclesRepository.findByStatus(status, pageable); }

    public String getDisplayStatus(Vehicle vehicle) {
        return statusConfig.getStatusDisplay(vehicle.getStatus());
    }

   

}
