package ru.yandex.javacource.novikov.schedule.manager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.javacource.novikov.schedule.manager.history.HistoryManager;
import ru.yandex.javacource.novikov.schedule.tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    private final HistoryManager inMemoryHistoryManager;
    private int generatorId = 0;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        inMemoryHistoryManager = Manager.getDefaultHistory();
    }

    //Добавляем новую задачу в Менеджер
    @Override
    public int addTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        tasks.put(task.getId(), task);
        return id;
    }

    //Добавляем новый эпик в Менеджер
    @Override
    public int addEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    //Добавляем новую подзадачу в менеджер и в епик соотвественно
    @Override
    public Integer addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtask(id);
        updateEpicStatus(epic);
        return id;
    }

    //Обновляем задачу
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask savedSubtask = subtasks.get(id);
        Epic savedEpic = epics.get(subtask.getEpicId());
        if (savedEpic == null || savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(savedEpic);
    }

    @Override
    public void updateEpic(Epic epic) {
        //Странно, все тесты проходили, но ошибку я понял свою, что история тоже обновится
        int id = epic.getId();
        Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasksIds(savedEpic.getSubtasks());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);

    }

    //Удаляем все задачи
    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    //Удаляем все эпики и все подзадачи
    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    //Удаляем все подзадачии обновляем статус епиков
    @Override
    public void removeAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            for (Epic epic : epics.values()) {
                epic.setStatus(Status.NEW);
            }
        }
    }

    //Удаляем задачу по id
    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    //Удаляем задачу по id
    @Override
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
    @Override
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
    @Override
    public List<Subtask> getAllEpicSubtasks(int epicId) {
        List<Subtask> returnSubtasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }

        return returnSubtasks;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {

        for (Task epic : epics.values()) {
            inMemoryHistoryManager.addTask(epic);
        }

        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) {
        inMemoryHistoryManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        inMemoryHistoryManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Task getTask(int id) {
        inMemoryHistoryManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    //Обновление статуса епика
    private void updateEpicStatus(Epic epic) {
        List<Integer> epicSubtasks = epic.getSubtasks();
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
