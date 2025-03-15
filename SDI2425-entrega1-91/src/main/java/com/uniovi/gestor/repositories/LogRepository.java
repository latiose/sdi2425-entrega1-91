package com.uniovi.gestor.repositories;

import com.uniovi.gestor.entities.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends CrudRepository<Log, Long> {

    @Query("SELECT l FROM Log l ORDER BY l.timestamp DESC")
    List<Log> findOrderedByTimestampDesc();

    @Query("SELECT l FROM Log l WHERE l.logType=?1")
    List<Log> findByLogType(String type);
}
