package ru.yandex.javacource.novikov.schedule.manager.history;
import ru.yandex.javacource.novikov.schedule.tasks.*;

import java.util.List;
import java.util.ArrayList;
//Иногда не видно перенос строки из за Idea, которая пишет кто последний менял код и это выглядит как пропуск стрко:)

public class InMemoryHistoryManager implements HistoryManager {

    protected static int MAX_HISTORY_SIZE = 10;
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }


        if (history.size() >= MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
