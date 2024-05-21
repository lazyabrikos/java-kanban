import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Task task = new Task("Wash_Floor", "washig all floors");
        System.out.println(task.toString());
        Epic epic = new Epic("Купить подарки", "подарки для нового года");
        System.out.println(epic.toString());
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Купить коробку",
                "купить коробку для подарков",
                epic.getTaskId()
        );
        Subtask subtask2 = new Subtask("Купить елку", "купить елку к нг", epic.getTaskId());
        System.out.println(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        System.out.println(subtask);
        System.out.println(epic.toString());
        subtask.setStatus(Status.DONE);
        taskManager.addSubtask(subtask);
        taskManager.removeAllEpics();
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());

    }
}
