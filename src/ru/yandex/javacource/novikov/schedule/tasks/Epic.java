package ru.yandex.javacource.novikov.schedule.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasks;

    public Epic(String taskName, String description) {
        super(taskName, description);
        subtasks = new ArrayList<>();
        this.type = TaskType.EPIC;
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
                '}';
    }
}
