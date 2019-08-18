package com.wizerdshins.tasksmanager.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Integer id;
    private String message;
    private LocalDate dateCreate;
    private LocalDate dateComplete;
    private String status;

    private Company company;

    /* TODO override equals & hashcode */
    /* TODO add annotations */

    public Task() {}

    public Task(String message, Date dateCreate, String status) {
        this.message = message;
        this.dateCreate = LocalDate.now();
        this.status = "open";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(LocalDate dateComplete) {
        this.dateComplete = dateComplete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
