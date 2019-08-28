package com.wizerdshins.tasksmanager.entity;


import javax.persistence.*;
import java.util.Date;
import java.time.*;

//import java.time.LocalDate;
import java.time.format.*;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String message;
    @Column(name = "date_create")
    private LocalDate dateCreate;
    @Column(name = "date_complete")
    private LocalDate dateComplete;
    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
//            "dd-MM-yyyy HH:mm");
//    private String dateFormat;

    /* TODO override equals & hashcode */
    /* TODO add annotations */

    public Task() {}

    /* TODO delete unused parameter in task constructor */

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
