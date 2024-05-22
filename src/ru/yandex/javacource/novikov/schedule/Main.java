package ru.yandex.javacource.novikov.schedule;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        TaskManager taskManager = new TaskManager();
        taskManager.addEpic(epic);
        System.out.println(epic.toString());
        Subtask subtask = new Subtask("Купить коробку",
                "купить коробку для подарков",
                epic.getTaskId()
        );

        Subtask subtask2 = new Subtask("Купить ленточку",
                "купить ленту для подарков",
                epic.getTaskId()
        );

        System.out.println(subtask);
        System.out.println(subtask2);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        System.out.println(subtask);
        System.out.println(subtask2);
        System.out.println();
        taskManager.removeSubtask(2);
        System.out.println(epic);
        subtask2.setStatus(Status.DONE);
        taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.removeSubtask(3);
        System.out.println(taskManager.getAllEpics());
    }
}
