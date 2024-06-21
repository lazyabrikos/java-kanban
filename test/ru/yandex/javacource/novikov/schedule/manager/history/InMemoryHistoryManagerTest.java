package ru.yandex.javacource.novikov.schedule.manager.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

public class InMemoryHistoryManagerTest {

    protected HistoryManager historyManager;
    protected Task task;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Test history", "Test history description");
        task.setId(1);
    }

    @Test
    public void historySizeShouldBe1() {

        historyManager.addTask(task);
        historyManager.addTask(task);

        Assertions.assertEquals(1, historyManager.getHistory().size(),
                "History manager dont save info"
        );
    }

    @Test
    public void historySizeShouldBe1After50AddsOfEqualsTasks() {

        for (int i = 0; i < 50; i++) {
            historyManager.addTask(task);
        }

        Assertions.assertEquals(1, historyManager.getHistory().size(),
                "History size is larger then MAX_HISTORY_SIZE"
        );
    }

    @Test
    public void historySizeShouldBe3AfterAdding3DiffTasks() {
        Task task2 = new Task("Task2", "New task2");
        task2.setId(2);
        Task task3 = new Task("Task3", "New task3");
        task3.setId(3);
        historyManager.addTask(task);
        historyManager.addTask(task2);
        historyManager.addTask(task3);

        Assertions.assertEquals(3, historyManager.getHistory().size(),
                "History size shoulde be equal 3"
        );
    }

    @Test
    public void historySizeShouldBe0AfterRemovingOneTask() {
        historyManager.addTask(task);
        historyManager.remove(task.getId());

        Assertions.assertEquals(0, historyManager.getHistory().size(),"" +
                "History size shoulde be equal 0"
        );
    }

    @Test
    public void historySizeShouldBeEqual2AfterRemoving1TaskFromHistorySize() {
        historyManager.addTask(task);
        Task task2 = new Task("Task2", "New task2");
        task2.setId(2);
        Task task3 = new Task("Task3", "New task3");
        task3.setId(3);
        historyManager.addTask(task);
        historyManager.addTask(task2);
        historyManager.addTask(task3);

        historyManager.remove(task2.getId());

        Assertions.assertEquals(2, historyManager.getHistory().size(),
                "History size should be equal 2"
        );
    }
}
