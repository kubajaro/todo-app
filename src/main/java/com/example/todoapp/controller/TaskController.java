package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> getAllTasks() {
        logger.warn("Exposing all entries");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> getAllTasks(Pageable page) {
        logger.info("Find all with pageable");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Optional<Task>> getTaskById(@PathVariable int id) {
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskRepository.findById(id));
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> saveTask(@RequestBody @Valid Task taskToSave) {
       return ResponseEntity.created(URI.create("/" + taskRepository.save(taskToSave).getId())).build();
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task taskToUpdate) {
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        taskToUpdate.setId(id);
        taskRepository.save(taskToUpdate);
        return ResponseEntity.noContent().build();
    }

}
