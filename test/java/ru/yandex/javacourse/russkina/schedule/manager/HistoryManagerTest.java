package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    public void shouldReturn3ForHistorySize() {
        TaskManager taskManager = Managers.getDefault();
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId()));
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.getTask(task.getId()); //удалил предыдущий просмотр задачи
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void shouldReturn1ForHistorySizeWhenTaskDeleted() {
        TaskManager taskManager = Managers.getDefault();
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId()));
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.deleteEpic(epic.getId()); // удалил эпик и его подзадачи
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void shouldReturnTrueWhenGetHistoryEquals() {
        TaskManager taskManager = Managers.getDefault();
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId()));
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        taskManager.deleteEpic(epic.getId()); // удалил эпик и его подзадачи
        List<Integer> historyIds = taskManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        assertEquals(List.of(1), historyIds);
    }
}