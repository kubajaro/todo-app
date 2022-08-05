package com.example.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id);

    List<Task> findAllByDone(@Param("status") boolean status);

    Task save(Task task);

    boolean existsById(Integer id);

    void deleteById(Integer id);
}
