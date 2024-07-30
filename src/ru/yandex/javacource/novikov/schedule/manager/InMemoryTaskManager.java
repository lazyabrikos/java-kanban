package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.exceptions.ValidationException;
import ru.yandex.javacource.novikov.schedule.manager.history.HistoryManager;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Status;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager inMemoryHistoryManager;
    private int generatorId = 0;
    private final Set<Task> prioritizedTask = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
    );

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        inMemoryHistoryManager = Manager.getDefaultHistory();
    }

    //Добавляем новую задачу в Менеджер
    @Override
    public int addTask(Task task) {
        validation(task);
        int id = ++generatorId;
        task.setId(id);
        tasks.put(task.getId(), task);
        prioritizedTask.add(task);
        return id;

    }

    //Добавляем новый эпик в Менеджер
    @Override
    public int addEpic(Epic epic) {
        int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        prioritizedTask.add(epic);
        return id;
    }

    //Добавляем новую подзадачу в менеджер и в епик соотвественно
    @Override
    public Integer addSubtask(Subtask subtask) {
        validation(subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtask(id);
        updateEpicData(epicId);
        prioritizedTask.add(subtask);
        return id;
    }

    //Обновляем задачу
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        prioritizedTask.remove(savedTask);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
        prioritizedTask.add(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask savedSubtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic savedEpic = epics.get(epicId);
        if (savedEpic == null || savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicData(epicId);
        prioritizedTask.remove(savedSubtask);
        prioritizedTask.add(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasksIds(savedEpic.getSubtasks());
        epic.setStatus(savedEpic.getStatus());
        updateEpicTime(epic);
        epics.put(epic.getId(), epic);
        prioritizedTask.remove(savedEpic);
        prioritizedTask.add(epic);
    }

    //Удаляем все задачи
    @Override
    public void removeAllTasks() {
        prioritizedTask.removeAll(tasks.values());
        tasks.clear();
    }

    //Удаляем все эпики и все подзадачи
    @Override
    public void removeAllEpics() {
        prioritizedTask.removeAll(epics.values());
        prioritizedTask.removeAll(subtasks.values());
        epics.clear();
        subtasks.clear();
    }

    //Удаляем все подзадачии обновляем статус епиков
    @Override
    public void removeAllSubtasks() {
        if (!subtasks.isEmpty()) {
            prioritizedTask.removeAll(subtasks.values());
            subtasks.clear();
            for (Epic epic : epics.values()) {
                epic.setStatus(Status.NEW);
                epic.setDuration(null);
                epic.setEndTime(null);
                epic.setStartTime(null);
            }
        }

    }

    //Удаляем задачу по id
    @Override
    public void removeTask(int id) {
        prioritizedTask.remove(tasks.get(id));
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    //Удаляем задачу по id
    @Override
    public void removeEpic(int id) {
        prioritizedTask.remove(epics.get(id));
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        inMemoryHistoryManager.remove(id);
        for (Integer subtasksId : epic.getSubtasks()) {
            prioritizedTask.remove(subtasks.get(subtasksId));
            subtasks.remove(subtasksId);
            inMemoryHistoryManager.remove(subtasksId);
        }
    }

    //Удаляем подзадачу по id
    @Override
    public void removeSubtask(int id) {
        prioritizedTask.remove(subtasks.get(id));
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        inMemoryHistoryManager.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtaskFromEpic(id);
        updateEpicData(epic.getId());
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
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        inMemoryHistoryManager.addTask(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        inMemoryHistoryManager.addTask(epic);
        return epic;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        inMemoryHistoryManager.addTask(task);
        return task;
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }


    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTask);
    }

    protected void addToHistory(Task task) {
        inMemoryHistoryManager.addTask(task);
    }

    //Обновление статуса епика
    private void updateEpicStatus(Epic epic) {
        List<Integer> epicSubtasks = epic.getSubtasks();
        int countEpicSubtasks = epicSubtasks.size();
        int countNewStatus = (int) epicSubtasks.stream()
                .filter(subtaskId -> subtasks.get(subtaskId).getStatus() == Status.NEW)
                .count();
        int countDoneStatus = (int) epicSubtasks.stream()
                .filter(subtaskId -> subtasks.get(subtaskId).getStatus() == Status.DONE)
                .count();

        if (countNewStatus == countEpicSubtasks) {
            epic.setStatus(Status.NEW);
        } else if (countDoneStatus == countEpicSubtasks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        if (epic != null) {
            List<Integer> subtaskIds = epic.getSubtasks();
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            Duration duration = null;

            for (Integer id : subtaskIds) {
                Subtask subtask = subtasks.get(id);
                if (startTime == null || subtask.getStartTime() != null && startTime.isAfter(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }

                if (endTime == null || subtask.getEndTime() != null && endTime.isBefore(subtask.getEndTime())) {
                    endTime = subtask.getEndTime();
                }

                if (duration == null) {
                    duration = subtask.getDuration();
                } else {

                    if (subtask.getDuration() != null) {
                        duration = duration.plusMinutes(subtask.getDuration().toMinutes());
                    }
                }
            }
            epic.setEndTime(endTime);
            epic.setStartTime(startTime);
            epic.setDuration(duration);
        }
    }

    private void updateEpicData(int id) {
        Epic epic = epics.get(id);
        updateEpicTime(epic);
        updateEpicStatus(epic);
    }

    protected void validation(Task task) {

        final LocalDateTime startTime = task.getStartTime();
        if (startTime == null) {
            return;
        }
        final LocalDateTime endTime = task.getEndTime();

        for (Task t : prioritizedTask) {
            if (t.getStartTime() != null && t.getDuration() != null) {
                final LocalDateTime existStart = t.getStartTime();
                final LocalDateTime existEnd = t.getEndTime();
                if (!endTime.isAfter(existStart)) {
                    continue;
                }

                if (!existEnd.isAfter(startTime)) {
                    continue;
                }

                throw new ValidationException("Задача пересекактся с id=" + t.getId() +
                        " c " + existStart + " по " + existEnd);
            }
        }
    }

    protected void setGeneratorId(int maxGeneratorId) {
        generatorId = maxGeneratorId;
    }

    protected void addTaskWithoutGeneratorId(Task task) {
        tasks.put(task.getId(), task);
    }

    protected void addEpicWithoutGeneratorId(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void addSubtaskWithoutGeneratorId(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int id = subtask.getId();
        subtasks.put(id, subtask);
        epic.addSubtask(id);
        updateEpicStatus(epic);
    }

}
