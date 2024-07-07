package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.*;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileBackedTaskManagerTest {

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected int taskId;
    protected int epicId;
    protected int subtaskId;
    private FileBackedTaskManager fileBackedTaskManager;
    protected List<Task> tasks;
    protected List<Epic> epics;
    protected List<Subtask> subtasks;
    protected File file;
    protected File historyFile;

    @BeforeEach
    public void createManager() {
        try {
            File path = new File(
                    "." + File.separator +
                            "test" + File.separator +
                            "ru" + File.separator +
                            "yandex" + File.separator +
                            "javacource" + File.separator +
                            "novikov" + File.separator +
                            "schedule" + File.separator +
                            "resources"
            );

            file = File.createTempFile("data", ".csv", path);
            historyFile = File.createTempFile("history", ".csv", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.setHistoryFile(historyFile);
        task = new Task("TestTask", "Description");
        taskId = fileBackedTaskManager.addTask(task);
        epic = new Epic("TestEpic", "Description");
        epicId = fileBackedTaskManager.addEpic(epic);
        subtask = new Subtask("TestSubtask", "Description");
        subtask.setEpicId(epicId);
        subtaskId = fileBackedTaskManager.addSubtask(subtask);

        tasks = fileBackedTaskManager.getAllTasks();
        epics = fileBackedTaskManager.getAllEpics();
        subtasks = fileBackedTaskManager.getAllSubtasks();
    }


    @Test
    public void addTaskTestAndFindItById() {
        final Task savedTask = fileBackedTaskManager.getTask(taskId);

        Assertions.assertNotNull(savedTask, "Task not found");

        Assertions.assertNotNull(tasks, "Tasks are not returned");
        Assertions.assertEquals(1, tasks.size(), "Count of tasks not equal");
    }

    @Test
    public void addEpicAndSubtaskAndFindItById() {
        final Epic savedEpic = fileBackedTaskManager.getEpic(epicId);

        Assertions.assertNotNull(savedEpic, "Epic not found");

        Assertions.assertNotNull(epics, "Epics are not returned");
        Assertions.assertEquals(1, epics.size(), "Count of epics not equal");


        final Subtask savedSubtask = fileBackedTaskManager.getSubtask(subtaskId);
        Assertions.assertNotNull(savedSubtask, "Subtask not found");

        Assertions.assertNotNull(subtasks, "Subtasks are not returned");
        Assertions.assertEquals(1, subtasks.size(), "Count of subtasks not equal");
    }

    @Test
    public void removeAllTaskTest() {
        fileBackedTaskManager.removeAllTasks();
        final List<Task> tasks = fileBackedTaskManager.getAllTasks();
        Assertions.assertEquals(0, tasks.size(), "Manager dont remove  tasks");
    }

    @Test
    public void removeAllEpicAndThatAllSubtasksRemovedAfterRemovingEpicTest() {
        fileBackedTaskManager.removeAllEpics();
        final List<Subtask> subtasks = fileBackedTaskManager.getAllSubtasks();
        final List<Epic> epics = fileBackedTaskManager.getAllEpics();
        Assertions.assertEquals(0, epics.size(), "Epic not removed");
        Assertions.assertEquals(0, subtasks.size(), "Subtasks not removed");
    }

    @Test
    public void removeAllSubtasksTest() {
        fileBackedTaskManager.removeAllSubtasks();
        final List<Subtask> subtasks = fileBackedTaskManager.getAllSubtasks();
        Assertions.assertEquals(0, subtasks.size(), "Subtaks not removed");
    }

    @Test
    public void removeTaskByIdTest() {
        fileBackedTaskManager.removeTask(taskId);
        Assertions.assertNull(fileBackedTaskManager.getTask(taskId), "Task not removed");
    }

    @Test
    public void removeEpicByIdTest() {
        fileBackedTaskManager.removeEpic(epicId);
        Assertions.assertNull(fileBackedTaskManager.getEpic(epicId), "Epic not removed");
    }

    @Test
    public void removeSubtaskByIdTest() {
        fileBackedTaskManager.removeSubtask(subtaskId);
        Assertions.assertNull(fileBackedTaskManager.getSubtask(subtaskId), "Subtask not removed");
    }

    @Test
    public void removeSubtaskFromEpicIfSubtaskRemovedFromManager() {
        fileBackedTaskManager.removeSubtask(subtaskId);
        Assertions.assertEquals(0, epic.getSubtasks().size(),
                "Subtask should be removed"
        );
    }

    @Test
    public void checkThatLoadFileWorksCorrect() {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(fileBackedTaskManager.getAllTasks(),
                fileBackedTaskManager1.getAllTasks(),
                "Tasks are not the same"
        );
        Assertions.assertEquals(fileBackedTaskManager.getAllEpics(),
                fileBackedTaskManager1.getAllEpics(),
                "Epics are not the same"
        );
        Assertions.assertEquals(fileBackedTaskManager.getAllSubtasks(),
                fileBackedTaskManager1.getAllSubtasks(),
                "Subtasks are not the same"
        );

    }


    @AfterEach
    public void deleteTempFiles() {
        file.deleteOnExit();
        historyFile.deleteOnExit();
    }
}
