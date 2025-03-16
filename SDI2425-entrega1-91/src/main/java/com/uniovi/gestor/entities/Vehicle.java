package com.uniovi.gestor.entities;

import com.uniovi.gestor.VehicleStatusConfig.VehicleStatus;

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
    private String numberPlate;
    @Column(unique=true)
    private String vin;
    private String brand;
    private String model;
    private double mileage;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<Journey> journeys = new HashSet<Journey>();
    private String fuel;

    public Vehicle(){}

    public Vehicle(String numberPlate, String vin, String brand, String model, String fuel,
                   double mileage, VehicleStatus status) {
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

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", numberPlate='" + numberPlate + '\'' +
                ", vin='" + vin + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", mileage=" + mileage +
                ", status=" + status +
                ", fuel='" + fuel + '\'' +
                '}';
    }

    public Set<Journey> getJourneys() {
        return journeys;
    }
}
