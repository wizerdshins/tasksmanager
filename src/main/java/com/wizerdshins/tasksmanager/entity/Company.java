package com.wizerdshins.tasksmanager.entity;

public class Company {

    private Integer id;
    private String name;
    private String address;
    private String phone;

    /* TODO add getters and setters */

    public Company() {}

    public Company(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


}
