package ru.yandex.javacource.novikov.schedule.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubtaskTest {

    @Test
    public void subtasksShouldBeEqualIfIdEqual() {
        Subtask firstSubtask = new Subtask("Subtask", "Test", 1);
        Subtask secondSubtask = new Subtask("Subtask", "Test", 1);
        firstSubtask.setId(10);
        secondSubtask.setId(10);

        Assertions.assertEquals(firstSubtask, secondSubtask);
    }
}
