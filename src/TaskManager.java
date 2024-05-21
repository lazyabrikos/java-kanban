import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    //Добавляем новую задачу в Менеджер
    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    //Добавляем новый эпик в Менеджер
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    //Добавляем новую подзадачу в менеджер и в епик соотвественно
    public void addSubtask(Subtask subtask) {
        epics.get(subtask.getEpicId()).addSubtask(subtask);
        subtasks.put(subtask.getTaskId(), subtask);
        Epic epic = epics.get(subtask.epicId);
        updateEpicStatus(epic);
    }

    //Обновление статуса епика
    private void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        int countEpicSubtasks = epicSubtasks.size();
        int countNewStatus = 0;
        int countDoneStatus = 0;
        for (Subtask subtask1 : epicSubtasks) {
            if (subtask1.getStatus() == Status.NEW) { countNewStatus++; }
            if (subtask1.getStatus() == Status.DONE) { countDoneStatus++; }
        }

        if (countNewStatus == countEpicSubtasks) {
            epic.setStatus(Status.NEW);
        } else if (countDoneStatus == countEpicSubtasks) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //Удаляем все задачи
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    //Удаляем все эпики и все подзадачи
    public void removeAllEpics(){
        if (!epics.isEmpty()) {
            epics.clear();
            subtasks.clear();
        }
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
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("No task with id=" + id);
        }
    }

    //Удаляем задачу по id
    public void removeEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getTaskId());
            }
        } else {
            System.out.println("No epic with id=" + id);
        }
    }

    //Удаляем подзадачу по id
    public void removeSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            epics.get(subtask.getEpicId()).removeSubtaskFromEpic(subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        } else {
            System.out.println("No subtask with id=" + id);
        }
    }


    //Получение списка всех подзадач эпика
    public ArrayList<Subtask> getAllEpicSubtasks(int epicId) {
        return epics.get(epicId).getSubtasks();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        return allTasks;

    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }
        return allEpics;

    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtask);
        }
        return allSubtasks;
    }

}
