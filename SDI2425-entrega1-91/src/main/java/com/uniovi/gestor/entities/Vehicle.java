package com.uniovi.gestor.entities;

import javax.persistence.*;

@Entity
@Table(name="vehicle")
public class Vehicle {

    public enum FuelType{
        GASOLINA,
        DIESEL,
        MICROHIBRIDO,
        HIBRIDO,
        ELECTRICO,
        GLP,
        GNL
    }

    @Id
    @GeneratedValue
    private long id;
    @Column(unique=true)
    private String numberPlate; // matrícula
    @Column(unique=true)
    private String vin; // número de bastidor
    private String brand;
    private String model;

    @Enumerated(EnumType.STRING)
    private FuelType fuel;

    public Vehicle(){}

    public Vehicle(String numberPlate, String vin, String brand, String model, FuelType fuel) {
        this.numberPlate = numberPlate;
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.fuel = fuel;
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

    public FuelType getFuel() {
        return fuel;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }
}
