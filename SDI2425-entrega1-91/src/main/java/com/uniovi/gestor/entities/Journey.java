package com.uniovi.gestor.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "journey")
public class Journey {

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

    public double getDuration() {
        return ChronoUnit.HOURS.between(endDate, startDate);
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

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private double odometerStart;
    private double odometerEnd;
}
