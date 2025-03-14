package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Log;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LogRepository extends CrudRepository<Log, Long> {

}
