package com.uniovi.notaneitor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Professor {
    @Id
    @GeneratedValue
    private Long id;
    private String DNI;
    private String name;
    private String surname;
    private Category category;

    public Professor(Long id, Category category, String surname, String name, String DNI) {
        this.id = id;
        this.category = category;
        this.surname = surname;
        this.name = name;
        this.DNI = DNI;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", DNI='" + DNI + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", category=" + category +
                '}';
    }

    public Professor(){

    }



}
