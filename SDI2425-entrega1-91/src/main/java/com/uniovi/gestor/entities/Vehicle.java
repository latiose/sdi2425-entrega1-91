package com.uniovi.gestor.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="vehicle")
public class Vehicle {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique=true)
    private String numberPlate; // matrícula
    @Column(unique=true)
    private String vin; // número de bastidor
    private String brand;
    private String model;
    private float mileage;
    private String status;
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<Journey> journeys = new HashSet<Journey>(); ;
    private String fuel;

    public Vehicle(){}

    public Vehicle(String numberPlate, String vin, String brand, String model, String fuel,
                   float mileage, String status) {
        this.numberPlate = numberPlate;
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.fuel = fuel;
        this.mileage = mileage;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
