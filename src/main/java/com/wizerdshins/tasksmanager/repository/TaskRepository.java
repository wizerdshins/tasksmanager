package com.wizerdshins.tasksmanager.repository;

import com.wizerdshins.tasksmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
