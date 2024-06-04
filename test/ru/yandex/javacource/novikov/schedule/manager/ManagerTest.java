package ru.yandex.javacource.novikov.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.manager.history.HistoryManager;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

public class ManagerTest {

    @Test
    public void shouldReturnTaskManager() {
        Assertions.assertNotNull(Manager.getDefault(), "Task Manager is null");
    }

    @Test
    public void shouldReturnHistoryManager() {
        Assertions.assertNotNull(Manager.getDefaultHistory(), "History Manager is null");
    }

}
