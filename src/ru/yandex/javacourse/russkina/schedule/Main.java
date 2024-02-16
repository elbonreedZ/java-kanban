package ru.yandex.javacourse.russkina.schedule;

import ru.yandex.javacourse.russkina.schedule.task.*;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Помыть посуду",
                "Тарелки на столе", Status.NEW));
        taskManager.createTask(new Task("Купить кроссовки",
                "Завтра", Status.NEW));
        taskManager.createEpic(new Epic("Убраться", "Послезавтра"));
        taskManager.createSubtask(new Subtask("Пропылесосить", "Сначала",
                Status.NEW, 3));
        taskManager.createSubtask(new Subtask("Помыть полы", "После",
                Status.NEW, 3));
        taskManager.createEpic(new Epic("Сделать дз", "Вечером"));
        taskManager.createSubtask(new Subtask("Математика", "номер 55, стр.24",
                Status.NEW, 6));

        taskManager.printTest();

        taskManager.updateTask(new Task("Помыть посуду",
                "Тарелки на столе", 1, Status.DONE));
        taskManager.updateSubtask(new Subtask("Математика",
                "номер 55, стр.24", 7, 6, Status.DONE));
        taskManager.updateSubtask(new Subtask("Пропылесосить", "Сначала", 4, 3,
                Status.DONE));
        taskManager.updateSubtask(new Subtask("Помыть полы", "После", 5, 3,
                Status.IN_PROGRESS));

        taskManager.printTest();

        taskManager.deleteSubtask(7);

        taskManager.printTest();

        taskManager.deleteEpic(3);

        taskManager.printTest();

    }
}
