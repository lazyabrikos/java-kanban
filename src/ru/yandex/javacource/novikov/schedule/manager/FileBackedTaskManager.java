package ru.yandex.javacource.novikov.schedule.manager;

import ru.yandex.javacource.novikov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.comporator.TaskComparator;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.FileReader;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String CSV_HEADER = "id,type,name,status,description,epic";
    private static File HISTORY_FILE = new File(
            "." + File.separator +
                    "resources" + File.separator + "history.csv"
    );
    private final File file;

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
    public Task getTask(int id) {
        Task task = super.getTask(id);
        saveHistory();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        saveHistory();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        saveHistory();
        return subtask;
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
        try (BufferedWriter bufferedWriter =  new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(CSV_HEADER);
            bufferedWriter.newLine();

            for (Task task : super.getAllTasks()) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
            }

            for (Epic epic : super.getAllEpics()) {
                bufferedWriter.write(toString(epic));
                bufferedWriter.newLine();
            }

            for (Subtask subtask : super.getAllSubtasks()) {
                bufferedWriter.write(toString(subtask));
                bufferedWriter.newLine();
            }
            saveHistory();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи сохраненных задача в файл");
        }
    }

    private void saveHistory() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(HISTORY_FILE, StandardCharsets.UTF_8))) {
            List<Task> history = super.getHistory();

            bufferedWriter.write(CSV_HEADER);
            bufferedWriter.newLine();
            for (Task task : history) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
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

            int maxId = -1;
            for (Task task : savedTasks) {
                maxId = Integer.max(maxId, task.getId());
                if (task.getType().equals(TaskType.EPIC)) {
                    taskManager.addEpicWithoutGeneratorId((Epic) task);
                } else if (task.getType().equals(TaskType.SUBTASK)) {
                    taskManager.addSubtaskWithoutGeneratorId((Subtask) task);
                } else {
                    taskManager.addTaskWithoutGeneratorId(task);
                }
            }
            taskManager.setGeneratorId(maxId);

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
        TaskType type = TaskType.valueOf(taskData[1]);
        String name = taskData[2];
        Status status = Status.valueOf(taskData[3]);
        String description = taskData[4];
        switch (type) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
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
        String[] csvString = new String[]{
                Integer.toString(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                task.getType().equals(TaskType.SUBTASK) ? Integer.toString(((Subtask) task).getEpicId()) : ""
        };
        return String.join(",", csvString);
    }

    protected void setHistoryFile(File historyFile) {
        HISTORY_FILE = historyFile;
    }

    protected String getHistoryFile() {
        return HISTORY_FILE.toString();
    }
}
