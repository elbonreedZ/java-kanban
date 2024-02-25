package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_SIZE = 10;

    private final List<Task> history;

    InMemoryHistoryManager() {
        history = new ArrayList<>();
    }


    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        history.add(task);
        updateHistory();
    }

    private void updateHistory() {
        if (history.size() > MAX_SIZE) {
            history.remove(0);
        }
    }

}
