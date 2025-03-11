package com.uniovi.gestor.services;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.repositories.RefuelsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefuelsService {
    private final RefuelsRepository refuelsRepository;

    public RefuelsService(RefuelsRepository refuelsRepository) {
        this.refuelsRepository = refuelsRepository;
    }

    public void addRefuel(Refuel refuel) {
        refuelsRepository.save(refuel);
    }

    public List<Refuel> getAllRefuels(){
        return refuelsRepository.findAll();
    }
}
