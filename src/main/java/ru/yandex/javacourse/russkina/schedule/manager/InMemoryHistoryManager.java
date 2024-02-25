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

    /* Я не намудрила, делать копии нужно для того, чтобы не было связи между задачами в одном и другом месте
     * В истории нужно сохранять задачи в том виде, в котором они туда попали (сделала такой вывод из одного из тестов,
     * где сказали проверить сохраняются ли предыдущие версии задач. С задачами и подзадачами все хорошо, ведь когда
     * они обновляются они перезаписываются, а вот эпики обновляют лишь поля сеттерами, и в истории эпик соответственно
     * получает, например, новый статус, что странно, когда остальные задачи не обновились. Эпики вы сказали не
     * перезаписывать. Для этого такая проверка*/

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            Epic newTask = new Epic(epic.getName(), epic.getDescription(), epic.getId());
            newTask.setStatus(epic.getStatus());
            newTask.setSubtasksId(epic.getSubtasksId());
            history.add(newTask);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            Subtask newTask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId(),
                    subtask.getEpicId(), subtask.getStatus());
            history.add(newTask);
        } else {
            Task newTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());
            history.add(newTask);
        }
        updateHistory();

    }

    private void updateHistory() {
        if (history.size() > MAX_SIZE) {
            history.remove(0);
        }
    }

}
