package ru.yandex.javacource.novikov.schedule.tasks;

import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    private int taskId;
    private Status status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(taskName, task.taskName) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (taskName != null) {
            hash = hash + Objects.hash(taskName);
        }
        hash = hash * 31;

        if (description != null) {
            hash = hash + Objects.hash(description);
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}
