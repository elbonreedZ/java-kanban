package ru.yandex.javacourse.russkina.schedule;

import ru.yandex.javacourse.russkina.schedule.task.*;
import ru.yandex.javacourse.russkina.schedule.manager.*;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

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


        taskManager.getSubtask(7);
        taskManager.getEpic(3);

        printAllTasks(taskManager);

        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getTask(1);
        taskManager.getSubtask(5);
        taskManager.getEpic(6);
        taskManager.getSubtask(5);
        taskManager.getSubtask(7);
        taskManager.getSubtask(5);
        taskManager.getEpic(3);

        taskManager.updateTask(new Task("Помыть посуду",
                "Тарелки на столе", 1, Status.DONE));
        taskManager.updateSubtask(new Subtask("Математика",
                "номер 55, стр.24", 7, 6, Status.DONE));
        taskManager.updateSubtask(new Subtask("Пропылесосить", "Сначала", 4, 3,
                Status.DONE));
        taskManager.updateSubtask(new Subtask("Помыть полы", "После", 5, 3,
                Status.IN_PROGRESS));

        printAllTasks(taskManager);

        taskManager.getSubtask(5);

        taskManager.getEpic(6);

        taskManager.deleteSubtask(7);

        printAllTasks(taskManager);

        taskManager.getEpic(3);

        taskManager.deleteEpic(3);

        printAllTasks(taskManager);

    }
    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();
    }
}
