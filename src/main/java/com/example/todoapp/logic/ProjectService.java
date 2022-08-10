package com.example.todoapp.logic;

import com.example.todoapp.TaskConfigurationProperties;
import com.example.todoapp.model.*;
import com.example.todoapp.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (config.isAllowMultipleTasksFromTemplate()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group for project is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                            var targetGroup = new TaskGroup();
                            targetGroup.setDescription(project.getDescription());
                            targetGroup.setTasks(
                                    project.getSteps().stream()
                                            .map(projectStep -> new Task(
                                                    projectStep.getDescription(),
                                                    deadline.plusDays(projectStep.getDaysToDeadline())
                                            )).collect(Collectors.toSet())
                            );
                            return targetGroup;
                        }
                ).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
