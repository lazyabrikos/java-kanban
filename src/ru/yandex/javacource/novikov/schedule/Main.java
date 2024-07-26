package ru.yandex.javacource.novikov.schedule;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.manager.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        Epic epic2 = new Epic("Приготовить новогодний стол", "Придумать разные блюда");
        Task task1 = new Task(
                "Сходить в магазин",
                "Купить продукты",
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 12))
        );
        Task task2 = new Task(
                "Позвонить родственникам",
                "Поздравить с Новым годом",
                Duration.ofMinutes(200),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        Task task3 = new Task("Пить пиво", "Поздравить с Новым годом");
        final File file = new File(
                "." + File.separator +
                        "resources" + File.separator + "data.csv"
        );

        TaskManager taskManager = Manager.getInMemoryTaskManager();
        int epicId = taskManager.addEpic(epic);
        int epic2Id = taskManager.addEpic(epic2);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        System.out.println(taskManager.getPrioritizedTasks());
        /*
        Subtask subtask = new Subtask("Купить коробку",
                "купить коробку для подарков",
                epicId,
                Duration.ofMinutes(100),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 40))
        );

        Subtask subtask2 = new Subtask("Купить ленточку",
                "купить ленту для подарков",
                epicId,
                Duration.ofMinutes(20),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 40))
        );
        Subtask subtask3 = new Subtask("Купить упаковку",
                "купить упаковку для подарков",
                epicId,
                Duration.ofMinutes(40),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 40))
        );

        int subtaskId = taskManager.addSubtask(subtask);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        System.out.println(taskManager.getPrioritizedTasks());

        Task tmp = taskManager.getEpic(epicId);
        tmp = taskManager.getTask(task1Id);
        tmp = taskManager.getEpic(epicId);
        tmp = taskManager.getEpic(epic2Id);
        tmp = taskManager.getSubtask(subtaskId);
        taskManager.removeEpic(epicId);

        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(file);
        System.out.println("History taskManager2");
        taskManager1.addTask(task1);
        taskManager1.addTask(task3);
        System.out.println(taskManager1.getPrioritizedTasks());
        */
    }
}
