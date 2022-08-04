package com.example.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
}
