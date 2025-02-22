package com.uniovi.notaneitor.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Mark {
    @Id
    @GeneratedValue
    private Long id;
    private boolean resend = false;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Employee getUser() {
        return employee;
    }

    public void setUser(Employee employee) {
        this.employee = employee;
    }

    public Mark(Long id, String description, Double score) {
        this.id = id;
        this.description = description;
        this.score = score;
    }

    public Mark(String description, Double score, Employee employee) {
        this.description = description;
        this.score = score;
        this.employee = employee;
    }



    public Mark(){

    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(id, mark.id);
    }


    private String description;
    private Double score;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }
    public Boolean getResend() {
        return resend;
    }
    public void setResend(Boolean resend) {
        this.resend = resend;
    }



}
