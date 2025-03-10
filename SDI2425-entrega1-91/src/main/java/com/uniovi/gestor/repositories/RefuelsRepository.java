package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Refuel;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.data.repository.CrudRepository;

public interface RefuelsRepository extends CrudRepository<Refuel, Long> {
}
