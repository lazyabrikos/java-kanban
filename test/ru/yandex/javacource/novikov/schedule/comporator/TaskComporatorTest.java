package ru.yandex.javacource.novikov.schedule.comporator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

public class TaskComporatorTest {

    protected TaskComparator taskComparator;
    protected Task task1;
    protected Task task2;

    @BeforeEach
    public void createTaskComporator() {
        taskComparator = new TaskComparator();
    }

    @Test
    public void checkThatFiratTaskLessThenSecondIfFirstIdLessThenSecond() {
        task1 = new Task("Tmp1", "tmp1");
        task2 = new Task("Tmp2", "tmp2");
        task1.setId(5);
        task2.setId(6);
        Assertions.assertTrue(taskComparator.compare(task1, task2) < 0,
                "Task1 should be less then Task2"
        );
    }

    @Test
    public void checkThatFiratTaskEqualSecondIfFirstIdEqualSecond() {
        task1 = new Task("Tmp1", "tmp1");
        task2 = new Task("Tmp2", "tmp2");
        task1.setId(6);
        task2.setId(6);
        Assertions.assertTrue(taskComparator.compare(task1, task2) == 0,
                "Task1 should be less then Task2"
        );
    }

    @Test
    public void checkThatFiratTaskGreaterThenSecondIfFirstIdEqualGreaterSecond() {
        task1 = new Task("Tmp1", "tmp1");
        task2 = new Task("Tmp2", "tmp2");
        task1.setId(7);
        task2.setId(6);
        Assertions.assertTrue(taskComparator.compare(task1, task2) > 0,
                "Task1 should be less then Task2"
        );
    }
}
