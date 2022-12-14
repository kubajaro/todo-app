package com.example.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup taskGroup);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
