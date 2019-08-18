package com.wizerdshins.tasksmanager.entity;


import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

public class Task {

    private Integer id;
    private String message;
    private LocalDate dateCreate;
    private LocalDate dateComplete;
    private String status;

    private Company company;

    /* TODO add getters and setters */
    /* TODO add annotations */

    public Task() {}

    public Task(String message, Date dateCreate, String status) {
        this.message = message;
        this.dateCreate = LocalDate.now();
        this.status = "open";
    }
}
