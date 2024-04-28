package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.exception.TaskValidationException;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    static TaskManager taskManager;
    static Task task;
    static Epic epic;
    static Subtask subtask;

    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getDefault();
        task = taskManager.createTask(new Task("name", "description", Status.NEW, 90,
                LocalDateTime.of(2024, 2,2,23,50)));
        epic = taskManager.createEpic(new Epic("name", "description"));
        subtask = taskManager.createSubtask(new Subtask("name", "description", Status.NEW, epic.getId(), 90,
                LocalDateTime.of(2023, 2,2,23,50)));
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
                "name", "description", task.getId(), Status.NEW, 90,
                LocalDateTime.of(2022, 2,2,23,50)));
        assertNotEquals(inputIdTask, task, "Две задачи созданы с одинаковым айди");
        Task inputIdEpic = taskManager.createEpic(new Epic(
                "name", "description", epic.getId()));
        assertNotEquals(inputIdEpic, epic, "Две задачи созданы с одинаковым айди");
        Task inputIdSubtask = taskManager.createSubtask(new Subtask(
                "name", "description", subtask.getId(), inputIdEpic.getId(), Status.NEW, 90,
                LocalDateTime.of(2021, 2,2,23,50)));
        assertNotEquals(inputIdSubtask, subtask, "Две задачи созданы с одинаковым айди");
    }

    @Test
    public void shouldReturnTrueWhenTaskCreateInTaskManagerEqualsTaskInManager() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW, 90,
                LocalDateTime.of(2020, 2,2,23,50)));
        Task taskFromTaskManager = taskManager.getTask(task.getId());
        assertEquals(task.getName(), taskFromTaskManager.getName());
        assertEquals(task.getDescription(), taskFromTaskManager.getDescription());
        assertEquals(task.getStatus(), taskFromTaskManager.getStatus());

    }

    @Test
    public void shouldReturnTrueWhenEpicCreateInTaskManagerEqualsEpicInManager() {
        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        Epic epicFromTaskManager = taskManager.getEpic(epic.getId());
        assertEquals(epic.getName(), epicFromTaskManager.getName());
        assertEquals(epic.getDescription(), epicFromTaskManager.getDescription());
        assertEquals(epic.getSubtasksId(), epicFromTaskManager.getSubtasksId());
    }

    @Test
    public void shouldReturnTrueWhenSubtaskCreateInTaskManagerEqualsSubtaskInManager() {
        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId(), 90,
                LocalDateTime.of(2019, 2,2,23,50)));
        Subtask subtaskFromTaskManager = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask.getName(), subtaskFromTaskManager.getName());
        assertEquals(subtask.getDescription(), subtaskFromTaskManager.getDescription());
        assertEquals(subtask.getStatus(), subtaskFromTaskManager.getStatus());
        assertEquals(subtask.getEpicId(), subtaskFromTaskManager.getEpicId());
    }

    @Test
    public void shouldReturnFalseWhenSubtaskDeletedInEpic() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId(), 90,
                LocalDateTime.of(2018, 2,2,23,50)));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId(), 90,
                LocalDateTime.of(2017, 2,2,23,50)));
        int idSub1 = subtask1.getId();
        taskManager.deleteSubtask(idSub1);
        assertFalse(epic1.getSubtasksId().contains(idSub1));
    }

    @Test
    public void shouldReturnFalseInOldEpicSubtaskIdWhenSubtaskSetEpicId() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        Subtask subtask1 = taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(), 90,
                        LocalDateTime.of(2016, 2,2,23,50)));
        Epic epic2 = taskManager.createEpic(new Epic("name", "description"));
        taskManager.setEpicId(epic2.getId(), subtask1);
        assertFalse(epic1.getSubtasksId().contains(subtask1.getId()));
    }

    @Test
    public void shouldReturnTrueForEpicStatusNewWhenSubtasksAllNew() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(), 90,
                        LocalDateTime.of(2015, 2,2,23,50)));
       taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(), 90,
                        LocalDateTime.of(2014, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(), 90,
                        LocalDateTime.of(2013, 2,2,23,50)));
        assertEquals(Status.NEW, taskManager.getEpic(epic1.getId()).getStatus());
    }

    @Test
    public void shouldReturnTrueForEpicStatusDoneWhenSubtasksAllDone() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.DONE, epic1.getId(), 90,
                        LocalDateTime.of(2012, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.DONE, epic1.getId(), 90,
                        LocalDateTime.of(2011, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.DONE, epic1.getId(), 90,
                        LocalDateTime.of(2010, 2,2,23,50)));
        assertEquals(Status.DONE, taskManager.getEpic(epic1.getId()).getStatus());
    }

    @Test
    public void shouldReturnTrueForEpicStatusInProgressWhenSubtasksNewAndDone() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(),90,
                        LocalDateTime.of(2009, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.NEW, epic1.getId(), 90,
                        LocalDateTime.of(2008, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.DONE, epic1.getId(), 90,
                        LocalDateTime.of(2007, 2,2,23,50)));
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1.getId()).getStatus());
    }

    @Test
    public void shouldReturnTrueForEpicStatusInProgressWhenSubtasksAllInProgress() {
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(), 90,
                        LocalDateTime.of(2006, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(), 90,
                        LocalDateTime.of(2005, 2,2,23,50)));
        taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(), 90,
                        LocalDateTime.of(2004, 2,2,23,50)));
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1.getId()).getStatus());
    }

    @Test
    public void shouldReturnNullWhenTaskTimeOverlapAnotherTaskTime() {
        taskManager.createTask(new Task("name", "description", Status.NEW,
                90, LocalDateTime.of(2024, 2, 3, 18,40)));
        Epic epic1 = taskManager.createEpic(new Epic("name", "description"));
        assertThrows(TaskValidationException.class,() -> taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(),
                        180, LocalDateTime.of(2024, 2, 3, 16,40))));
        Subtask subtask2 = taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(),
                        180, LocalDateTime.of(2024, 2, 3, 14,40)));
        assertThrows(TaskValidationException.class,() ->taskManager.createSubtask(
                new Subtask("name", "descr", Status.IN_PROGRESS, epic1.getId(),
                        180, LocalDateTime.of(2024, 2, 3, 18,45))));
        Task task2 = taskManager.createTask(new Task("name", "description", Status.NEW,
                90, LocalDateTime.of(2024, 2, 3, 20,20)));
        assertNotNull(subtask2);
        assertNotNull(task2);
    }
}