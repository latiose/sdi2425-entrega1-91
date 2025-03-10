package com.uniovi.gestor.services;


import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.repositories.JourneysRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

}
