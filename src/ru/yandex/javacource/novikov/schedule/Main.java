package ru.yandex.javacource.novikov.schedule;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.manager.*;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        TaskManager taskManager = Manager.getDefault();
        System.out.println(taskManager);
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

        System.out.println(subtask);
        System.out.println(subtask2);
        System.out.println("Add subtasks");
        int subtaskId = taskManager.addSubtask(subtask);
        int subtask2Id = taskManager.addSubtask(subtask2);
        System.out.println(subtask);
        System.out.println(subtask2);
        System.out.println();
        System.out.println();
        System.out.println("Remove first subtask");
        taskManager.removeSubtask(subtaskId);
        System.out.println(epic);
        subtask2.setStatus(Status.DONE);
        System.out.println("Update status second subtask");
        taskManager.updateSubtask(subtask2);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("Remove second subtask");
        taskManager.removeSubtask(subtask2Id);
        System.out.println(taskManager.getAllEpics());

    }
}
