package com.example.todoapp.controller;

import com.example.todoapp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    private DataSourceProperties dateSource;
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dateSource, TaskConfigurationProperties myProp) {
        this.dateSource = dateSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dateSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
