package ru.yandex.javacource.novikov.schedule.manager.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.manager.Manager;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

public class InMemoryHistoryManagerTest {

    @Test
    public void historySizeShouldBe2() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Test history", "Test history description");

        historyManager.addTask(task);
        historyManager.addTask(task);

        Assertions.assertEquals(2, historyManager.getHistory().size(),
                "History manager dont save info"
        );

    }
}
