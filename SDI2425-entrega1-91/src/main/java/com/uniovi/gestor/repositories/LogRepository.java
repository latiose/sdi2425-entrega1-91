package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
