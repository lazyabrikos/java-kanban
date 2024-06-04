package ru.yandex.javacource.novikov.schedule.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {

    @Test
    public void epicsShouldBeEqualIfIdEquals() {
        Epic firstEpic = new Epic("Epic", "Test");
        Epic secondEpic = new Epic("Epic", "Test");

        firstEpic.setId(10);
        secondEpic.setId(10);

        Assertions.assertEquals(firstEpic, secondEpic);
    }

}
