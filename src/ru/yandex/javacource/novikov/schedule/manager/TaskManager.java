package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;
import java.util.List;

public interface TaskManager {
    //Добавляем новую задачу в Менеджер
    int addTask(Task task);

    //Добавляем новый эпик в Менеджер
    int addEpic(Epic epic);

    //Добавляем новую подзадачу в менеджер и в епик соотвественно
    Integer addSubtask(Subtask subtask);

    //Обновляем задачу
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    //Удаляем все задачи
    void removeAllTasks();

    //Удаляем все эпики и все подзадачи
    void removeAllEpics();

    //Удаляем все подзадачии обновляем статус епиков
    void removeAllSubtasks();

    //Удаляем задачу по id
    void removeTask(int id);

    //Удаляем задачу по id
    void removeEpic(int id);

    //Удаляем подзадачу по id
    void removeSubtask(int id);

    //Получение списка всех подзадач эпика
    List<Subtask> getAllEpicSubtasks(int epicId);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getHistory();
}
