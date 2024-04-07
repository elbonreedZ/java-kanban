package ru.yandex.javacourse.russkina.schedule.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.yandex.javacourse.russkina.schedule.manager.Managers;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;

class TaskTest {
    static TaskManager taskManager;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldReturnTrueIfTaskIdsEquals() {
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.NEW);
        assertEquals(task1, task2);
    }

    @Test
    public void shouldReturnTrueIfEpicIdsEquals() {
        Epic epic1 = new Epic("name1", "description1", 2);
        Epic epic2 = new Epic("name2", "description2", 2);
        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldReturnTrueIfSubtaskIdsEquals() {
        Subtask subtask1 = new Subtask("name1", "description1", 10, 2, Status.NEW);
        Subtask subtask2 = new Subtask("name2", "description2", 10, 2, Status.NEW);
        assertEquals(subtask1, subtask2);
    }


}