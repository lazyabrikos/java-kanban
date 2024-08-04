package ru.yandex.javacource.novikov.schedule.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description);
        this.type = TaskType.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime(LocalDateTime endTime) {
        return endTime;
    }

    public void addSubtask(Integer subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubtaskFromEpic(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasksIds(List<Integer> subtasksIds) {
        subtasks = subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks.size() +
                ", taskName='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getId() +
                ", status=" + this.getStatus() +
                ", type=" + this.getType().toString() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.endTime +
                '}';
    }
}
