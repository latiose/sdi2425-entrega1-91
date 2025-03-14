package com.uniovi.gestor.services;

import com.uniovi.gestor.entities.Log;
import com.uniovi.gestor.repositories.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public void log(String logType, String description) {
        Log log = new Log(logType, description);
        logRepository.save(log);
        logger.info("Log registrado: [{}] - {}", logType, description);
    }
}
