package com.example.todoapp.controller;

import com.example.todoapp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    private DataSourceProperties dateSource;
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dateSource, TaskConfigurationProperties myProp) {
        this.dateSource = dateSource;
        this.myProp = myProp;
    }

    @GetMapping("/info/url")
    String url() {
        return dateSource.getUrl();
    }

    @GetMapping("/info/prop")
    boolean myProp() {
        return myProp.isAllowMultipleTasksFromTemplate();
    }
}
