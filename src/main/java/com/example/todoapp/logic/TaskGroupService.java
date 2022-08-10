package com.example.todoapp.logic;

import com.example.todoapp.TaskConfigurationProperties;
import com.example.todoapp.model.TaskGroup;
import com.example.todoapp.model.TaskGroupRepository;
import com.example.todoapp.model.TaskRepository;
import com.example.todoapp.model.projection.GroupReadModel;
import com.example.todoapp.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;
    //private TaskConfigurationProperties config;

    public TaskGroupService(final TaskGroupRepository taskGroupRepository
                            //final TaskConfigurationProperties config
                            ) {
        this.repository = taskGroupRepository;
        //this.config = config;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Complete all tasks first!");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalStateException("Task with given id not found"));
        result.setDone(!result.isDone());
    }
}
