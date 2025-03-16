package com.uniovi.gestor.entities;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String dni;
    private String name;
    private String lastName;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Journey> journeys = new HashSet<>();
    private String password;

    @Transient
    private String passwordConfirm;

    @Transient String newPassword;
    @Transient String confirmNewPassword;

    private String role;


    public Employee(String dni, String name, String lastName) {
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
    }
    public Employee() { }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDni() {return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @SuppressWarnings("unused")
    public String getFullName() {
        return this.name + " " + this.lastName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNewPassword() { return newPassword; }
    @SuppressWarnings("unused")
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmNewPassword() { return confirmNewPassword; }
    @SuppressWarnings("unused")
    public void setConfirmNewPassword(String confirmNewPassword) { this.confirmNewPassword = confirmNewPassword; }

    public Set<Journey> getJourneys() {
        return journeys;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}