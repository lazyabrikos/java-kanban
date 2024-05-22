package ru.yandex.javacource.novikov.schedule.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasks;

    public Epic(String taskName, String description) {
        super(taskName, description);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Integer subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubtaskFromEpic(Integer subtaskId) {
        subtasks.remove(subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks.size() +
                ", taskName='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
