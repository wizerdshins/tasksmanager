package com.wizerdshins.tasksmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wizerdshins.tasksmanager.entity.Company;

import java.io.File;
import java.util.List;

public class WriterService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public WriterService() {}

    public void write(String path, List<Company> list) throws Exception {
        File file = new File(path);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
    }

}
