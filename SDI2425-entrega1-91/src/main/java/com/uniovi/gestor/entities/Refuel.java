package com.uniovi.gestor.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="refuel")
public class Refuel {
    @Id
    @GeneratedValue
    private long id;
    private String station;
    private double price;
    private double amount;
    private boolean fullTank;
    private double odometer;
    private String comments;
    private LocalDateTime date;

    @ManyToOne
    private Journey journey;

    public Refuel() {}

    public Refuel(String station, double price, double amount, boolean fullTank, double odometer, String comments) {
        this.station = station;
        this.price = price;
        this.amount = amount;
        this.fullTank = fullTank;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isFullTank() {
        return fullTank;
    }

    public void setFullTank(boolean fullTank) {
        this.fullTank = fullTank;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public String getDayMonthYear(){
        return date.getDayOfMonth()+"/"+date.getMonthValue() + "/" + date.getYear();
    }

    public String getHoursMinutes(){
        return date.getHour()+":"+date.getMinute();
    }

    public Vehicle getVehicle(){
        return journey.getVehicle();
    }

    public double getTotalPrice(){
        return price*amount;
    }
}
