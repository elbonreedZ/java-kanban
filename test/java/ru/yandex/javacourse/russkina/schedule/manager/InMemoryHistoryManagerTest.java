package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getDefault();
        task = taskManager.createTask(new Task("name", "description", Status.NEW, 90,
                LocalDateTime.of(2024, 2,2,23,50)));
        epic = taskManager.createEpic(new Epic("name", "description"));
        subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId(), 90,
                LocalDateTime.of(2023, 2,2,23,50)));
    }

    @Test
    public void shouldReturn1ForHistorySize() {
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.getTask(task.getId()); //удалил предыдущий просмотр задачи
        taskManager.deleteEpic(epic.getId()); // удалил эпик и его подзадачи
        assertEquals(1, taskManager.getHistory().size());
    }

}