package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager taskManager;
    static Task task;
    static Epic epic;
    static Subtask subtask;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getDefault();
        task = taskManager.createTask(new Task("name", "description", Status.NEW));
        epic = taskManager.createEpic(new Epic("name", "description"));
        subtask = taskManager.createSubtask(new Subtask("name", "description", Status.NEW, epic.getId()));
    }

    @Test
    public void shouldReturnNullIfSubtaskMakeItselfItsOwnEpic() {
        Subtask subtask1 = taskManager.createSubtask(new Subtask(
                "name1",
                "description1",
                Status.NEW, subtask.getId()));
        assertNull(subtask1);
    }

    @Test
    public void shouldReturnTasksInTaskManager() {
        assertNotNull(taskManager.getTask(task.getId()));
        assertNotNull(taskManager.getEpic(epic.getId()));
        assertNotNull(taskManager.getSubtask(subtask.getId()));
        assertNotNull(taskManager.getTasks());
        assertNotNull(taskManager.getSubtasks());
        assertNotNull(taskManager.getEpics());
    }

    @Test
    public void shouldReturnEpicInTaskManager() {
        assertNotNull(taskManager.getEpic(epic.getId()));
    }

    @Test
    public void shouldReturnSubtaskInTaskManager() {
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    public void shouldReturnTrueIfIdsNotEquals() {
        Task inputIdTask = taskManager.createTask(new Task(
                "name", "description", task.getId(), Status.NEW));
        assertNotEquals(inputIdTask, task, "Две задачи созданы с одинаковым айди");
        Task inputIdEpic = taskManager.createEpic(new Epic(
                "name", "description", epic.getId()));
        assertNotEquals(inputIdEpic, epic, "Две задачи созданы с одинаковым айди");
        Task inputIdSubtask = taskManager.createSubtask(new Subtask(
                "name", "description", subtask.getId(), inputIdEpic.getId(), Status.NEW));
        assertNotEquals(inputIdSubtask, subtask, "Две задачи созданы с одинаковым айди");
    }

    @Test
    public void shouldReturnTrueWhenTaskCreateInTaskManagerEqualsTaskInManager() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Task taskFromTaskManager = taskManager.getTask(task.getId());
        assertEquals(task.getName(), taskFromTaskManager.getName());
        assertEquals(task.getDescription(), taskFromTaskManager.getDescription());
        assertEquals(task.getStatus(), taskFromTaskManager.getStatus());

        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        Epic epicFromTaskManager = taskManager.getEpic(epic.getId());
        assertEquals(epic.getName(), epicFromTaskManager.getName());
        assertEquals(epic.getDescription(), taskFromTaskManager.getDescription());
        assertEquals(epic.getSubtasksId(), epicFromTaskManager.getSubtasksId());

        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId()));
        Subtask subtaskFromTaskManager = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask.getName(), subtaskFromTaskManager.getName());
        assertEquals(subtask.getDescription(), subtaskFromTaskManager.getDescription());
        assertEquals(subtask.getStatus(), subtaskFromTaskManager.getStatus());
        assertEquals(subtask.getEpicId(), subtaskFromTaskManager.getEpicId());
    }

}