package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.manager.history.HistoryManager;
import ru.yandex.javacource.novikov.schedule.manager.history.InMemoryHistoryManager;

import java.io.File;

public final class Manager {

    private Manager() {

    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File(
                "." + File.separator +
                        "resources" + File.separator + "data.csv"
            )
        );
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
