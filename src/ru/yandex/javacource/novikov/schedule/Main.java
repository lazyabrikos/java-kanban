package ru.yandex.javacource.novikov.schedule;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.manager.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        Epic epic2 = new Epic("Приготовить новогодний стол", "Придумать разные блюда");
        Task task1 = new Task("Сходить в магазин", "Купить продукты");
        Task task2 = new Task("Позвонить родственникам", "Поздравить с Новым годом");
        File file = new File("D:\\IdeaProjects\\java-kanban\\src\\ru\\yandex\\javacource\\novikov\\schedule\\resources\\data.csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int epicId = taskManager.addEpic(epic);
        int epic2Id = taskManager.addEpic(epic2);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        System.out.println(epic.toString());
        Subtask subtask = new Subtask("Купить коробку",
                "купить коробку для подарков",
                epicId
        );

        Subtask subtask2 = new Subtask("Купить ленточку",
                "купить ленту для подарков",
                epicId
        );
        Subtask subtask3 = new Subtask("Купить упаковку",
                "купить упаковку для подарков",
                epicId
        );
        int subtaskId = taskManager.addSubtask(subtask);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);

        Task tmp = taskManager.getTask(task1Id);
        tmp = taskManager.getTask(task2Id);
        tmp = taskManager.getEpic(epicId);
        tmp = taskManager.getEpic(epic2Id);
        System.out.println(taskManager.getHistory());
        tmp = taskManager.getSubtask(subtaskId);
        System.out.println(taskManager.getHistory());
        taskManager.removeTask(task1Id);
        System.out.println(taskManager.getHistory());
        taskManager.removeEpic(epicId);
        System.out.println(taskManager.getHistory());

        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        System.out.println("History taskManager2");
        System.out.println(taskManager1.getHistory());


    }
}
