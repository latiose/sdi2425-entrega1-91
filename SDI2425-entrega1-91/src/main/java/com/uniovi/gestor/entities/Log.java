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

    public String toString(){
        return timestamp + " | " + this.logType + " - " + this.description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getLogType() {
        return logType;
    }
    @SuppressWarnings("unused")
    public void setLogType(String logType) {
        this.logType = logType;
    }
    @SuppressWarnings("unused")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    @SuppressWarnings("unused")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
