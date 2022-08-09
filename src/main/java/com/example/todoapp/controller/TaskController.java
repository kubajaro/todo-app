package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> findTaskById(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @PostMapping("/tasks")
    ResponseEntity<Task> saveTask(@RequestBody @Valid Task taskToSave) {
        return ResponseEntity.created(URI.create("/" + taskRepository.save(taskToSave).getId())).build();
    }

    @PutMapping("tasks/{id}")
    ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody @Valid Task taskToUpdate) {
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> {task.updateFrom(taskToUpdate);
                taskRepository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tasks/{id}")
    ResponseEntity<Valid> deleteTaskById(@PathVariable int id) {
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PatchMapping ("tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if(!taskRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

}
