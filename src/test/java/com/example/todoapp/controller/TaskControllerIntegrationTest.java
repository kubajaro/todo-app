package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        //given
        int id = taskRepository.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void httpGet_returnsToggledTasks() throws Exception {
        //given
        var taskToSaveId = taskRepository.save(new Task("test", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/" + taskToSaveId))
                .andExpect(status().is2xxSuccessful());
    }
}