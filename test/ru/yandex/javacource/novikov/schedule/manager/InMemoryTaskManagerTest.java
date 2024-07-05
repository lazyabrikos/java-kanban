package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Status;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;
import java.util.List;

public class InMemoryTaskManagerTest {

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected int epicId;
    protected int taskId;
    protected int subtaskId;
    protected List<Task> tasks;
    protected List<Epic> epics;
    protected List<Subtask> subtasks;
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task("TestTask", "Description");
        taskId = inMemoryTaskManager.addTask(task);
        epic = new Epic("TestEpic", "Description");
        epicId = inMemoryTaskManager.addEpic(epic);
        subtask = new Subtask("TestSubtask", "Description");
        subtask.setEpicId(epicId);
        subtaskId = inMemoryTaskManager.addSubtask(subtask);

        tasks = inMemoryTaskManager.getAllTasks();
        epics = inMemoryTaskManager.getAllEpics();
        subtasks = inMemoryTaskManager.getAllSubtasks();
    }

    @Test
    public void addTaskTestAndFindItById() {
        final Task savedTask = inMemoryTaskManager.getTask(taskId);
        Assertions.assertNotNull(savedTask, "Task not found");

        Assertions.assertNotNull(tasks, "Tasks are not returned");
        Assertions.assertEquals(1, tasks.size(), "Count of tasks not equal");
    }

    @Test
    public void addEpicAndSubtaskAndFindItById() {
        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        Assertions.assertNotNull(savedEpic, "Epic not found");

        Assertions.assertNotNull(epics, "Epics are not returned");
        Assertions.assertEquals(1, epics.size(), "Count of epics not equal");


        final Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        Assertions.assertNotNull(savedSubtask, "Subtask not found");

        Assertions.assertNotNull(subtasks, "Subtasks are not returned");
        Assertions.assertEquals(1, subtasks.size(), "Count of subtasks not equal");
    }

    @Test
    public void tasksShouldNotChangeAfterAddingToManager() {
        final Task savedTask = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(task, savedTask, "Tasks not equals");
        Assertions.assertEquals(task, tasks.get(0), "Tasks not equal");

        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(epic, savedEpic, "Epics not equal");
        Assertions.assertEquals(epic, epics.get(0), "Epics not equal");

        final Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        Assertions.assertEquals(subtask, savedSubtask, "Subtask not equal");
        Assertions.assertEquals(subtask, subtasks.get(0), "Subtasks not equal");
    }

    @Test
    public void countOfTasksShouldBe2AndIdNotEqualWhenFirstTaskHasIdAndSecondTaskGenerateIdWhenAdding() {
        Task secondTask = new Task("Test secondTaskId", "Test secondTaskId description");
        secondTask.setId(10);
        final int secondTaskId = inMemoryTaskManager.addTask(secondTask);
        final List<Task> tasks = inMemoryTaskManager.getAllTasks();
        Assertions.assertEquals(2, tasks.size(), "Count of tasks not equal");
        Assertions.assertNotEquals(taskId, secondTaskId, "Tasks id's equal");
    }

    @Test
    public void removeAllTaskTest() {
        inMemoryTaskManager.removeAllTasks();
        final List<Task> tasks = inMemoryTaskManager.getAllTasks();
        Assertions.assertEquals(0, tasks.size(), "Manager dont remove  tasks");
    }

    @Test
    public void removeAllEpicAndThatAllSubtasksRemovedAfterRemovingEpicTest() {
        inMemoryTaskManager.removeAllEpics();
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtasks();
        final List<Epic> epics = inMemoryTaskManager.getAllEpics();
        Assertions.assertEquals(0, epics.size(), "Epic not removed");
        Assertions.assertEquals(0, subtasks.size(), "Subtasks not removed");
    }

    @Test
    public void removeAllSubtasksTest() {
        inMemoryTaskManager.removeAllSubtasks();
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtasks();
        Assertions.assertEquals(0, subtasks.size(), "Subtaks not removed");
    }

    @Test
    public void removeTaskByIdTest() {
        inMemoryTaskManager.removeTask(taskId);
        Assertions.assertNull(inMemoryTaskManager.getTask(taskId), "Task not removed");
    }

    @Test
    public void removeEpicByIdTest() {
        inMemoryTaskManager.removeEpic(epicId);
        Assertions.assertNull(inMemoryTaskManager.getEpic(epicId), "Epic not removed");
    }

    @Test
    public void removeSubtaskByIdTest() {
        inMemoryTaskManager.removeSubtask(subtaskId);
        Assertions.assertNull(inMemoryTaskManager.getSubtask(subtaskId), "Subtask not removed");
    }

    @Test
    public void updateTaskTestThatCountOfTaskNotChanged() {
        task.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task);
        final List<Task> tasks = inMemoryTaskManager.getAllTasks();
        Assertions.assertEquals(1, tasks.size(), "Manager added new task againts updating it");
    }

    @Test
    public void updateSubtaskTestThatCountOfSubtasksNotChanged() {
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtasks();
        Assertions.assertEquals(1, subtasks.size(), "Manager added new subtask againts updating it");
    }

    @Test
    public void updateEpicTestThatCountOfEpicsNotChanged () {
        epic.setName("NEEEEEEEEEEW");
        inMemoryTaskManager.updateEpic(epic);
        final List<Epic> epics = inMemoryTaskManager.getAllEpics();
        Assertions.assertEquals(1, epics.size(), "Manager added new epic againts updating it");

    }

    @Test
    public void epicStatusShouldBeInProgressAfterSubtaskStatusChangeToInProgress() {
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        Epic savedEpic = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicStatusShouldBeDoneAfterSubtaskStatusChangeToDone() {
        subtask.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask);
        Epic savedEpic = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(Status.DONE, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicStatusShouldBeInProgressWhen1SubtaskInProgressAnd2SubtaskIsDone() {
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        Subtask subtask2 = new Subtask("Teeeest", "Description", epicId);
        final int subtask2Id = inMemoryTaskManager.addSubtask(subtask2);
        Epic savedEpic = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicInHistoryShouldBeUpdated() {
        Epic firstEpic = inMemoryTaskManager.getEpic(epicId);
        Epic epic1 = new Epic("NewTest", "Description");
        epic1.setId(epicId);
        inMemoryTaskManager.updateEpic(epic1);
        Epic savedEpic = inMemoryTaskManager.getEpic(epicId);
        List<Task> history = inMemoryTaskManager.getHistory();
        Assertions.assertEquals(savedEpic, history.get(0), "Epic should be updated");
    }

    @Test
    public void taskInHistoryShouldBeUpdated() {
        Task firstTask = inMemoryTaskManager.getTask(taskId);
        Task task1 = new Task("NewTask", "Description");
        task1.setId(taskId);
        inMemoryTaskManager.updateTask(task1);
        Task savedTask = inMemoryTaskManager.getTask(taskId);
        List<Task> history = inMemoryTaskManager.getHistory();
        Assertions.assertEquals(savedTask, history.get(0), "Task should be updated");
    }

    @Test
    public void subtaskInHistoryShouldBeUpdated() {
        Subtask firstSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        Subtask subtask1 = new Subtask("NewSubtask", "Description", epicId);
        subtask1.setId(subtaskId);
        inMemoryTaskManager.updateSubtask(subtask1);
        Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        List<Task> history = inMemoryTaskManager.getHistory();
        Assertions.assertEquals(savedSubtask, history.get(0), "Subtask should be updated");
    }

    @Test
    public void removeSubtaskFromEpicIfSubtaskRemovedFromManager() {
        inMemoryTaskManager.removeSubtask(subtaskId);
        Assertions.assertEquals(0, epic.getSubtasks().size(),
                "Subtask should be removed"
        );
    }
}
