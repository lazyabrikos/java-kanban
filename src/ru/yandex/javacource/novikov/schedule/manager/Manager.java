package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.manager.history.HistoryManager;
import ru.yandex.javacource.novikov.schedule.manager.history.InMemoryHistoryManager;

public final class Manager {

    private Manager() {}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
