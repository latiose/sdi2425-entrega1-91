package com.uniovi.gestor.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 1000)
    private String description;

    public Log() {}

    public Log(String logType, String description) {
        this.logType = logType;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }
}
