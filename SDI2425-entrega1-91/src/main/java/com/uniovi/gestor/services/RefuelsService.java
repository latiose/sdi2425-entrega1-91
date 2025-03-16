package com.uniovi.gestor.services;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.repositories.RefuelsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RefuelsService {
    private final RefuelsRepository refuelsRepository;
    private final JourneysService journeysService;

    public RefuelsService(RefuelsRepository refuelsRepository, JourneysService journeysService) {
        this.refuelsRepository = refuelsRepository;
        this.journeysService = journeysService;
    }

    public void addRefuel(Refuel refuel) {
        refuel.setJourney(getActiveJourney());
        refuelsRepository.save(refuel);
    }

    public Page<Refuel> getAllRefuels(Pageable pageable){
        return refuelsRepository.findAll(pageable);
    }

    public Journey getActiveJourney(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        return journeysService.findActiveJourneyByDni(dni);
    }

    public Page<Refuel> getRefuelsByNumberPlate(String numberPlate, Pageable pageable) {
        return refuelsRepository.findByNumberPlate(numberPlate, pageable);
    }
}
