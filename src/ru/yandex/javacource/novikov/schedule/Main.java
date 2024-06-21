package ru.yandex.javacource.novikov.schedule;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.manager.*;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        TaskManager taskManager = Manager.getDefault();
        int epicId = taskManager.addEpic(epic);
        System.out.println(epic.toString());
        Subtask subtask = new Subtask("Купить коробку",
                "купить коробку для подарков",
                epic.getId()
        );

        Subtask subtask2 = new Subtask("Купить ленточку",
                "купить ленту для подарков",
                epic.getId()
        );
        Subtask subtask3 = new Subtask("Купить упаковку",
                "купить упаковку для подарков",
                epic.getId()
        );

        int subtaskId = taskManager.addSubtask(subtask);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        System.out.println(subtaskId);
        System.out.println(subtask2Id);
        Task tmp = taskManager.getSubtask(subtaskId);
        System.out.println(taskManager.getHistory());
        tmp = taskManager.getSubtask(subtask2Id);
        System.out.println(taskManager.getHistory());
        tmp = taskManager.getSubtask(subtaskId);
        System.out.println(taskManager.getHistory());
        tmp = taskManager.getSubtask(subtask3Id);
        System.out.println(taskManager.getHistory());
        taskManager.removeSubtask(subtaskId);
        System.out.println(taskManager.getHistory());
    }
}
