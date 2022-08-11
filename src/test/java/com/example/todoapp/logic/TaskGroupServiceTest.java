package com.example.todoapp.logic;

import com.example.todoapp.model.TaskGroup;
import com.example.todoapp.model.TaskGroupRepository;
import com.example.todoapp.model.TaskRepository;
import org.assertj.core.api.GenericComparableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("method throws IllegalStateException due to uncompleted tasks")
    void toggleGroupTest_throwsException_tasksNotCompleted() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);

        //system under test
        TaskGroupService toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalStateException due to not existing Project Group")
    void toggleGroupTest_throwsException_noProjectGroupFound() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(any())).thenReturn(Optional.empty());

        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroupTest_shouldToggleGroup() {
        //given
        TaskGroup group = new TaskGroup();
        var beforeToggle = group.isDone();
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);

        //
        assertEquals(beforeToggle, !group.isDone());
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        var taskRepositoryMock = mock(TaskRepository.class);
        when(taskRepositoryMock.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return taskRepositoryMock;
    }
}