package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class CVSTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static Task fromString(String value) {
        String[] elements = value.split(",");
        Status status = Status.NEW;
        switch (elements[3]) {
            case "NEW":
                break;
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
        }
        switch (elements[1]) {
            case "TASK":
                return new Task(elements[2], elements[4], Integer.parseInt(elements[0]), status);
            case "EPIC":
                return new Epic(elements[2], elements[4], Integer.parseInt(elements[0]));
            case "SUBTASK":
                return new Subtask(elements[2], elements[4], Integer.parseInt(elements[0]), Integer.parseInt(elements[5]), status);
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder historyString = new StringBuilder();
        for (Task task : history) {
            historyString.append(task.getId());
            historyString.append(",");
        }
        if (historyString.length() > 0) {
            historyString.deleteCharAt(historyString.length() - 1); // Удаление последней запятой
        }
        return historyString.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] historyArray = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String elem : historyArray) {
            history.add(Integer.parseInt(elem));
        }
        return history;
    }

    public static String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d", subtask.getId(), task.getType(), subtask.getName(),
                    subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
        }
        return String.format("%d,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(),
                task.getDescription());
    }
}
