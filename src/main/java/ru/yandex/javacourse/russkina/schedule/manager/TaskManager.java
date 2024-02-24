package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void removeAllTasks();

    void removeEpics();

    void removeSubtasks();

    void removeTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    HistoryManager getHistoryManager();

}
