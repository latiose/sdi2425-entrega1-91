package com.uniovi.gestor.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "journey")
public class Journey {
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Id
    @GeneratedValue
    private long id;
    private double duration;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private double odometerStart;
    private double odometerEnd;

    @OneToMany(mappedBy = "journey")
    private Set<Refuel> refuels = new HashSet<>();

    public Journey(){
        this.startDate = LocalDateTime.now();

    }

    @Override
    public String toString() {
        return "Journey{" +
                "employee=" + employee +
                ", vehicle=" + vehicle +
                ", id=" + id +
                ", duration=" + duration +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", odometerStart=" + odometerStart +
                ", odometerEnd=" + odometerEnd +
                '}';
    }

    public Journey(Vehicle v){
        this();
        this.vehicle=v;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observaciones) {
        this.observations = observaciones;
    }

    private String observations;

    public double getDuration() {
        if(endDate!=null) return ChronoUnit.HOURS.between(startDate, endDate);
        return 0;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getOdometerEnd() {
        return odometerEnd;
    }

    public void setOdometerEnd(double odometerEnd) {
        this.odometerEnd = odometerEnd;
    }

    public double getOdometerStart() {
        return odometerStart;
    }

    public void setOdometerStart(double odometerStart) {
        this.odometerStart = odometerStart;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Set<Refuel> getRefuels() {
        return refuels;
    }

    public void setRefuels(Set<Refuel> refuels) {
        this.refuels = refuels;
    }

}
