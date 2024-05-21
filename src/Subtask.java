import java.util.ArrayList;

public class Subtask extends  Task {

    protected int epicId;

    Subtask(String taskName, String description) {
        super(taskName, description);
    }
    Subtask(String taskName, String description, int epicId) {
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
                ", taskName='" + this.getTaskId() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", taskId=" + this.getTaskId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
