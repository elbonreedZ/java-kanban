package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    public void shouldReturnTrueWhenAddTaskInHistoryManager() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        taskManager.getTask(task.getId());
        taskManager.updateTask(new Task("name2", "description2", task.getId(), Status.DONE));
        taskManager.getTask(task.getId());
        ArrayList<Task> history = taskManager.getHistoryManager().getHistory();
        assertEquals(2, history.size());
        Task taskInHistory = history.get(0);
        assertEquals(taskInHistory.getName(), task.getName(), "Значение имени обновилось");
        assertEquals(taskInHistory.getDescription(), task.getDescription(), "Значение описания обновилось");
        assertEquals(taskInHistory.getStatus(), task.getStatus(), "Значение статуса обновилось");

        Epic epic = taskManager.createEpic(new Epic("name", "description"));
        String name = epic.getName(); // тут нужно сохранить значения, так как эпик не перезаписывается,
        // а только обновляет свои поля
        String description = epic.getDescription();
        Status status = epic.getStatus();
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        taskManager.getEpic(epic.getId());
        taskManager.updateEpic(new Epic("name2", "description2", epic.getId()));
        taskManager.getEpic(epic.getId());
        assertEquals(4, history.size());
        Epic epicInHistory = (Epic) history.get(2);
        assertEquals(epicInHistory.getName(), name, "Значение имени обновилось");
        assertEquals(epicInHistory.getDescription(), description, "Значение описания обновилось");
        assertEquals(epicInHistory.getStatus(), status, "Значение статуса обновилось");
        assertEquals(epicInHistory.getSubtasksId(), subtasksId, "Список подзадач обновился");

        Subtask subtask = taskManager.createSubtask(new Subtask("name", "description",
                Status.NEW, epic.getId()));
        taskManager.getSubtask(subtask.getId());
        taskManager.updateSubtask(new Subtask("name2", "description2",
                subtask.getId(), subtask.getEpicId(), Status.DONE));
        taskManager.getTask(task.getId());
        assertEquals(6, history.size());
        Subtask subtaskInHistory = (Subtask) history.get(4);
        assertEquals(subtaskInHistory.getName(), subtask.getName(), "Значение имени обновилось");
        assertEquals(subtaskInHistory.getDescription(), subtask.getDescription(),
                "Значение описания обновилось");
        assertEquals(subtaskInHistory.getStatus(), subtask.getStatus(), "Значение статуса обновилось");
        assertEquals(subtaskInHistory.getEpicId(), subtask.getEpicId(), "id эпика обновилось");
    }

}