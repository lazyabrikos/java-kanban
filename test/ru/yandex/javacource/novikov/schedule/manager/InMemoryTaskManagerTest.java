package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Status;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{


    @BeforeEach
    public void createTaskManager() {
        manager = new InMemoryTaskManager();
        task = new Task("TestTask",
                "Description",
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 12))
        );
        taskId = manager.addTask(task);
        epic = new Epic("TestEpic", "Description");
        epicId = manager.addEpic(epic);
        subtask = new Subtask("TestSubtask",
                "Description",
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 12))
        );
        subtask.setEpicId(epicId);
        subtaskId = manager.addSubtask(subtask);

        tasks = manager.getAllTasks();
        epics = manager.getAllEpics();
        subtasks = manager.getAllSubtasks();
    }


}
