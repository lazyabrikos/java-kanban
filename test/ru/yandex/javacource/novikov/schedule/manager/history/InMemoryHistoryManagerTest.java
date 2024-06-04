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

    }

    @Test
    public void historySizeShouldBe2() {

        historyManager.addTask(task);
        historyManager.addTask(task);

        Assertions.assertEquals(2, historyManager.getHistory().size(),
                "History manager dont save info"
        );
    }

    @Test
    public void historySizeShouldBe10After50Adds () {

        for (int i = 0; i < 50; i++) {
            historyManager.addTask(task);
        }

        Assertions.assertEquals(10, historyManager.getHistory().size(),
                "History size is larger then MAX_HISTORY_SIZE"
        );
    }
}
