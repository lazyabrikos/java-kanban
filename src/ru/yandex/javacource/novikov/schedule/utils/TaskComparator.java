package ru.yandex.javacource.novikov.schedule.utils;

import ru.yandex.javacource.novikov.schedule.tasks.Task;
import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return task1.getId() - task2.getId();
    }

}
