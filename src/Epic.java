import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks;

    Epic(String taskName, String description) {
        super(taskName, description);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
        }
    }

    public void removeSubtaskFromEpic(Subtask subtask) {
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks.size() +
                ", taskName='" + this.getTaskName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getTaskId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
