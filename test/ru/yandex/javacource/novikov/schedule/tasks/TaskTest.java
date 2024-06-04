package ru.yandex.javacource.novikov.schedule.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void tasksShouldBeEqualIfIdEqual() {
        Task firstTask = new Task("Task", "Description");
        Task secondTask = new Task("Task", "Description");
        firstTask.setId(10);
        secondTask.setId(10);

        Assertions.assertEquals(firstTask, secondTask, "Tasks not equal");
    }


}
