package ru.yandex.javacource.novikov.schedule.manager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

import ru.yandex.javacource.novikov.schedule.tasks.*;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private int generatorId = 0;


    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    //Добавляем новую задачу в Менеджер
    public int addTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
            return task.getTaskId();
        }
        int id = ++generatorId;
        task.setTaskId(id);
        tasks.put(task.getTaskId(), task);
        return id;
    }

    //Добавляем новый эпик в Менеджер
    public int addEpic(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            return epic.getTaskId();
        }
        int id = ++generatorId;
        epic.setTaskId(id);
        epics.put(id, epic);
        return id;
    }

    //Добавляем новую подзадачу в менеджер и в епик соотвественно
    public Integer addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getTaskId())) {
            subtasks.put(subtask.getTaskId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
            return subtask.getTaskId();
        }
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = ++generatorId;
        subtask.setTaskId(id);
        subtasks.put(id, subtask);
        epic.addSubtask(id);
        updateEpicStatus(epic);
        return id;
    }

    //Удаляем все задачи
    public void removeAllTasks() {
        tasks.clear();
    }

    //Удаляем все эпики и все подзадачи
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    //Удаляем все подзадачии обновляем статус епиков
    public void removeAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            for (Epic epic : epics.values()) {
                epic.setStatus(Status.NEW);
            }
        }
    }

    //Удаляем задачу по id
    public void removeTask(int id) {
        tasks.remove(id);
    }

    //Удаляем задачу по id
    public void removeEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtasksId : epic.getSubtasks()) {
            subtasks.remove(subtasksId);
        }
    }

    //Удаляем подзадачу по id
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }

        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskFromEpic((Integer) id);
        updateEpicStatus(epic);
    }


    //Получение списка всех подзадач эпика
    public ArrayList<Integer> getAllEpicSubtasks(int epicId) {
        return epics.get(epicId).getSubtasks();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //Обновление статуса епика
    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicSubtasks = epic.getSubtasks();
        int countEpicSubtasks = epicSubtasks.size();
        int countNewStatus = 0;
        int countDoneStatus = 0;
        for (Integer subtaskId : epicSubtasks) {
            if (subtasks.get(subtaskId).getStatus() == Status.NEW) {
                countNewStatus++;
            }
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) {
                countDoneStatus++;
            }
        }

        if (countNewStatus == countEpicSubtasks) {
            epic.setStatus(Status.NEW);
        } else if (countDoneStatus == countEpicSubtasks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
