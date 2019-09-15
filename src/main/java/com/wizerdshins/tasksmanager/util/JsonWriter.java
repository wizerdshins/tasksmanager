package com.wizerdshins.tasksmanager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.wizerdshins.tasksmanager.entity.Company;

import java.io.File;
import java.util.List;

public class JsonWriter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JsonWriter() {}

    public void write(String path, List<Company> list) throws Exception {
        File file = new File(path + ".txt");
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
    }

}
