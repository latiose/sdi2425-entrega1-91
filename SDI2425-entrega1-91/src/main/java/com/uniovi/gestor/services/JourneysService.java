package com.uniovi.gestor.services;


import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.repositories.JourneysRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class JourneysService {
    private final JourneysRepository journeysRepository;

    public JourneysService(JourneysRepository journeysRepository) {
        this.journeysRepository = journeysRepository;
    }

    @PostConstruct
    public void init() {
    }

    public Page<Journey> getJourneys(Pageable pageable) {
        return journeysRepository.findAll(pageable);
    }

    public Journey getJourney(long id) { return journeysRepository.findById(id).get();}

    public void addJourney(Journey journey) {
        journeysRepository.save(journey);
    }

    public void deleteJourney(Journey journey) {
        journeysRepository.delete(journey);
    }

    public Vehicle findVehicleByNumberPlate(String plateNumber) {
        return journeysRepository.findByNumberPlate(plateNumber);
    }

    public List<Journey> findByVehicle(Vehicle v) {
        return journeysRepository.findByVehicle(v);
    }

    public Page<Journey> findByVehiclePageable(Vehicle v, Pageable pageable) {
        return journeysRepository.findByVehicle(v, pageable);
    }

    public List<Journey> findByDni(String dni) {
        return journeysRepository.findByDni(dni);
    }

    public Journey findActiveJourneyByDni(String dni) {
        return journeysRepository.findActiveJourneyByDni(dni);
    }

    public Page<Journey> findByDniPage(String dni, Pageable pageable){
        return journeysRepository.findByDniPage(dni, pageable);
    }

    public Vehicle findVehicleByJourney(Long id ) { return journeysRepository.findVehicleByJourneyId(id);
    }
}
