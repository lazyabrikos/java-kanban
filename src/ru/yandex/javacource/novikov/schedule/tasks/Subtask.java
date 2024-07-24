package ru.yandex.javacource.novikov.schedule.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String taskName, String description) {
        super(taskName, description);
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String taskName, String description, Duration duration) {
        super(taskName, description, duration);
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String taskName, String description, Duration duration, LocalDateTime startTime) {
        super(taskName, description, duration, startTime);
        this.type = TaskType.SUBTASK;
    }


    public Subtask(String taskName, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(taskName, description, duration, startTime);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
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
                ", taskName='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getId() +
                ", status=" + this.getStatus() +
                ", type=" + this.getType().toString() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                '}';
    }
}
