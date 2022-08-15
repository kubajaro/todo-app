package com.example.todoapp.controller;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerLighterIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        //given
        var description = "foo";
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));

        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(description)));
    }

    @Test
    void httpGet_returnsToggledTasks() throws Exception {
        //given
        when(taskRepository.existsById(anyInt())).thenReturn(true);
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(new Task("test", LocalDateTime.now())));

        //when + then
        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/" + 1))
                .andExpect(status().is2xxSuccessful());
    }
}