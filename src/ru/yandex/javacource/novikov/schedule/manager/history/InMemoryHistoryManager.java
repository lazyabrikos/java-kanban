package ru.yandex.javacource.novikov.schedule.manager.history;
import ru.yandex.javacource.novikov.schedule.tasks.*;

import java.util.List;
import java.util.ArrayList;
public class InMemoryHistoryManager implements HistoryManager{

    protected static int MAX_HISTORY_SIZE = 10;
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        if (history.size() < MAX_HISTORY_SIZE) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
