package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.*;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    protected File file;
    protected File historyFile;

    @BeforeEach
    public void createManager() {
        file = new File(
                "." + File.separator +
                        "resources" + File.separator + "test.data.csv"
        );

        historyFile = new File(
                "." + File.separator +
                        "resources" + File.separator + "test.history.csv"
        );

        manager = new FileBackedTaskManager(file);
        manager.setHistoryFile(historyFile);
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

    @Test
    public void checkThatLoadFileWorksCorrect() {
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(manager.getAllTasks(),
                manager1.getAllTasks(),
                "Tasks are not the same"
        );
        Assertions.assertEquals(manager.getAllEpics(),
                manager1.getAllEpics(),
                "Epics are not the same"
        );
        Assertions.assertEquals(manager.getAllSubtasks(),
                manager1.getAllSubtasks(),
                "Subtasks are not the same"
        );

    }

    @AfterEach
    public void deleteTempFiles() {
        file.deleteOnExit();
        historyFile.deleteOnExit();
    }
}
