package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.utils.TaskComparator;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.FileReader;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager  {
    private final File file;
    private final static String CSV_HEADER = "id,type,name,status,description,epic";
    private final static File HISTORY_FILE = new File("D:\\IdeaProjects\\java-kanban\\src\\ru\\yandex\\javacource\\novikov\\schedule\\resources\\history.csv");
    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public int addTask(Task task) {
        int taskId = super.addTask(task);
        save();
        return taskId;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        int subtaskId = super.addSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public int addEpic(Epic epic) {
        int epicId = super.addEpic(epic);
        save();
        return epicId;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(CSV_HEADER+"\n");

            for (Task task : super.getAllTasks()) {
                fileWriter.write(toString(task)+"\n");
            }

            for (Epic epic : super.getAllEpics()) {
                fileWriter.write(toString(epic)+"\n");
            }

            for (Subtask subtask : super.getAllSubtasks()) {
                fileWriter.write(toString(subtask)+"\n");
            }
            saveHistory();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи сохраненных задача в файл");
        }
    }

    private void saveHistory() {
        try (FileWriter fileWriter = new FileWriter(HISTORY_FILE, StandardCharsets.UTF_8)) {
            List<Task> history = super.getInMemoryHistoryManager().getHistory();

            fileWriter.write(CSV_HEADER+"\n");
            for (Task task : history) {
                fileWriter.write(toString(task)+"\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении истории в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        List<Task> savedTasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            loadHistory(taskManager);

            String line = br.readLine();
            while (br.ready()) {
               line = br.readLine();
               if (line != null) {
                   Task task = fromString(line);
                   savedTasks.add(task);
               }
           }

            TaskComparator taskComparator = new TaskComparator();

           savedTasks.sort(taskComparator);

           for (Task task : savedTasks) {
               if (task instanceof Epic) {
                   taskManager.addEpic((Epic) task);
               } else if (task instanceof Subtask) {
                   taskManager.addSubtask((Subtask) task);
               } else {
                   taskManager.addTask(task);
               }
           }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла");
        }


        return taskManager;
    }

    private static void loadHistory(FileBackedTaskManager taskManager) {
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                if (line != null) {
                    Task task = fromString(line);
                    taskManager.addToHistory(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтение файла с историей");

        }

    }

    private static Task fromString(String taskString) {
        String[] taskData = taskString.split(",");
        int id = Integer.parseInt(taskData[0]);
        String type = taskData[1];
        String name = taskData[2];
        Status status = Status.valueOf(taskData[3]);
        String description = taskData[4];
        switch (type) {
            case "TASK":
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(name, description);
                subtask.setId(id);
                subtask.setStatus(status);
                int epicId = Integer.parseInt(taskData[5]);
                subtask.setEpicId(epicId);
                return subtask;
            default:
                return null;
        }
    }

    private String toString(Task task) {
        String[] csvString = new String[] {
                Integer.toString(task.getId()),
                getType(task).toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                getSubtaskEpicId(task)
        };
        return String.join(",", csvString);
    }

    private String getSubtaskEpicId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }

        return TaskType.TASK;
    }
}
