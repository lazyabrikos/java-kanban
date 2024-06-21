package ru.yandex.javacource.novikov.schedule.manager.history;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import java.util.List;

public interface HistoryManager {
    void addTask(Task task);

    void remove(int id);

    List<Task> getHistory();

}
