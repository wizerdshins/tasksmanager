package com.wizerdshins.tasksmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class TasksmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksmanagerApplication.class, args);
    }

}
