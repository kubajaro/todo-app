package com.example.todoapp.controller;

import com.example.todoapp.logic.ProjectService;
import com.example.todoapp.model.Project;
import com.example.todoapp.model.ProjectStep;
import com.example.todoapp.model.projection.ProjectWriteModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth) {
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("project", new ProjectWriteModel());
            return "projects";
        }
        model.addAttribute("project", new ProjectWriteModel());
        return "index";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try {
            service.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grup??!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "B????d podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}