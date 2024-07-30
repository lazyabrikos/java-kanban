package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.exceptions.ValidationException;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Status;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected int epicId;
    protected int taskId;
    protected int subtaskId;
    protected List<Task> tasks;
    protected List<Epic> epics;
    protected List<Subtask> subtasks;

    @Test
    public void addTaskTestAndFindItById() {
        final Task savedTask = manager.getTask(taskId);
        Assertions.assertNotNull(savedTask, "Task not found");

        Assertions.assertNotNull(tasks, "Tasks are not returned");
        Assertions.assertEquals(1, tasks.size(), "Count of tasks not equal");
    }

    @Test
    public void addEpicAndSubtaskAndFindItById() {
        final Epic savedEpic = manager.getEpic(epicId);

        Assertions.assertNotNull(savedEpic, "Epic not found");

        Assertions.assertNotNull(epics, "Epics are not returned");
        Assertions.assertEquals(1, epics.size(), "Count of epics not equal");


        final Subtask savedSubtask = manager.getSubtask(subtaskId);
        Assertions.assertNotNull(savedSubtask, "Subtask not found");

        Assertions.assertNotNull(subtasks, "Subtasks are not returned");
        Assertions.assertEquals(1, subtasks.size(), "Count of subtasks not equal");
    }

    @Test
    public void tasksShouldNotChangeAfterAddingToManager() {
        final Task savedTask = manager.getTask(taskId);
        Assertions.assertEquals(task, savedTask, "Tasks not equals");
        Assertions.assertEquals(task, tasks.get(0), "Tasks not equal");

        final Epic savedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(epic, savedEpic, "Epics not equal");
        Assertions.assertEquals(epic, epics.get(0), "Epics not equal");

        final Subtask savedSubtask = manager.getSubtask(subtaskId);
        Assertions.assertEquals(subtask, savedSubtask, "Subtask not equal");
        Assertions.assertEquals(subtask, subtasks.get(0), "Subtasks not equal");
    }

    @Test
    public void countOfTasksShouldBe2AndIdNotEqualWhenFirstTaskHasIdAndSecondTaskGenerateIdWhenAdding() {
        Task secondTask = new Task("Test secondTaskId", "Test secondTaskId description");
        secondTask.setId(10);
        final int secondTaskId = manager.addTask(secondTask);
        final List<Task> tasks = manager.getAllTasks();
        Assertions.assertEquals(2, tasks.size(), "Count of tasks not equal");
        Assertions.assertNotEquals(taskId, secondTaskId, "Tasks id's equal");
    }

    @Test
    public void removeAllTaskTest() {
        manager.removeAllTasks();
        final List<Task> tasks = manager.getAllTasks();
        Assertions.assertEquals(0, tasks.size(), "Manager dont remove  tasks");
    }

    @Test
    public void removeAllEpicAndThatAllSubtasksRemovedAfterRemovingEpicTest() {
        manager.removeAllEpics();
        final List<Subtask> subtasks = manager.getAllSubtasks();
        final List<Epic> epics = manager.getAllEpics();
        Assertions.assertEquals(0, epics.size(), "Epic not removed");
        Assertions.assertEquals(0, subtasks.size(), "Subtasks not removed");
    }

    @Test
    public void removeAllSubtasksTest() {
        manager.removeAllSubtasks();
        final List<Subtask> subtasks = manager.getAllSubtasks();
        Assertions.assertEquals(0, subtasks.size(), "Subtaks not removed");
    }

    @Test
    public void removeTaskByIdTest() {
        manager.removeTask(taskId);
        Assertions.assertNull(manager.getTask(taskId), "Task not removed");
    }

    @Test
    public void removeEpicByIdTest() {
        manager.removeEpic(epicId);
        Assertions.assertNull(manager.getEpic(epicId), "Epic not removed");
    }

    @Test
    public void removeSubtaskByIdTest() {
        manager.removeSubtask(subtaskId);
        Assertions.assertNull(manager.getSubtask(subtaskId), "Subtask not removed");
    }

    @Test
    public void updateTaskTestThatCountOfTaskNotChanged() {
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        final List<Task> tasks = manager.getAllTasks();
        Assertions.assertEquals(1, tasks.size(), "Manager added new task againts updating it");
    }

    @Test
    public void updateSubtaskTestThatCountOfSubtasksNotChanged() {
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        final List<Subtask> subtasks = manager.getAllSubtasks();
        Assertions.assertEquals(1, subtasks.size(), "Manager added new subtask againts updating it");
    }

    @Test
    public void updateEpicTestThatCountOfEpicsNotChanged() {
        epic.setName("NEEEEEEEEEEW");
        manager.updateEpic(epic);
        final List<Epic> epics = manager.getAllEpics();
        Assertions.assertEquals(1, epics.size(), "Manager added new epic againts updating it");

    }

    @Test
    public void epicStatusShouldBeInProgressAfterSubtaskStatusChangeToInProgress() {
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        Epic savedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicStatusShouldBeDoneAfterSubtaskStatusChangeToDone() {
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);
        Epic savedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.DONE, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicStatusShouldBeInProgressWhen1SubtaskInProgressAnd2SubtaskIsDone() {
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        Subtask subtask2 = new Subtask("Teeeest",
                "Description",
                epicId,
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 40))
        );
        final int subtask2Id = manager.addSubtask(subtask2);
        Epic savedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Status not changed");
    }

    @Test
    public void epicStatusShouldBeInProgressWhen1SubtaskIsNewAnd2IsDone() {
        subtask.setStatus(Status.DONE);
        Subtask subtask2 = new Subtask("Teeeest",
                "Description",
                epicId,
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 40))
        );
        final int subtask2Id = manager.addSubtask(subtask2);
        Epic savedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Status is not IN_Progress");
    }

    @Test
    public void epicInHistoryShouldBeUpdated() {
        Epic firstEpic = manager.getEpic(epicId);
        Epic epic1 = new Epic("NewTest", "Description");
        epic1.setId(epicId);
        manager.updateEpic(epic1);
        Epic savedEpic = manager.getEpic(epicId);
        List<Task> history = manager.getHistory();
        Assertions.assertEquals(savedEpic, history.get(0), "Epic should be updated");
    }

    @Test
    public void taskInHistoryShouldBeUpdated() {
        Task firstTask = manager.getTask(taskId);
        Task task1 = new Task("NewTask", "Description");
        task1.setId(taskId);
        manager.updateTask(task1);
        Task savedTask = manager.getTask(taskId);
        List<Task> history = manager.getHistory();
        Assertions.assertEquals(savedTask, history.get(0), "Task should be updated");
    }

    @Test
    public void subtaskInHistoryShouldBeUpdated() {
        Subtask firstSubtask = manager.getSubtask(subtaskId);
        Subtask subtask1 = new Subtask(
                "NewSubtask",
                "Description",
                epicId,
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 40))
        );
        subtask1.setId(subtaskId);
        manager.updateSubtask(subtask1);
        Subtask savedSubtask = manager.getSubtask(subtaskId);
        List<Task> history = manager.getHistory();
        Assertions.assertEquals(savedSubtask, history.get(0), "Subtask should be updated");
    }

    @Test
    public void removeSubtaskFromEpicIfSubtaskRemovedFromManager() {
        manager.removeSubtask(subtaskId);
        Assertions.assertEquals(0, epic.getSubtasks().size(),
                "Subtask should be removed"
        );
    }

    @Test
    public void checkValidationWorksCorrectAndThrowException() {
        Task task2 = new Task(
                "Tmp",
                "tmp",
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0))
        );
        Assertions.assertThrows(ValidationException.class,
                () -> manager.addTask(task2),
                "Валидация не работает"
        );
    }


    @Test
    public void checkValidationWorksCorrectAndDoNotThrowException() {
        Task task2 = new Task(
                "Tmp",
                "tmp",
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0))
        );
        Assertions.assertDoesNotThrow(
                () -> manager.addTask(task2),
                "Пересечение по времени"
        );
    }
}
