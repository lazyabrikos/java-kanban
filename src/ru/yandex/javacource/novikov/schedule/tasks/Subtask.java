package ru.yandex.javacource.novikov.schedule.tasks;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String taskName, String description) {
        super(taskName, description);
    }

    public Subtask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getTaskId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
