package ru.yandex.javacourse.russkina.schedule;

import ru.yandex.javacourse.russkina.schedule.server.HttpTaskServer;
import ru.yandex.javacourse.russkina.schedule.task.*;
import ru.yandex.javacourse.russkina.schedule.manager.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static ru.yandex.javacourse.russkina.schedule.manager.FileBackedTaskManager.loadFromFile;

public class Main {
    public static void main(String[] args) {
        TaskManager fileBackedTaskManager = null;
        try {
            File file = File.createTempFile("java-kanban-file", ".cvs");
            fileBackedTaskManager = loadFromFile(file);
            HttpTaskServer taskServer = new HttpTaskServer();
            taskServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

     private static void printMenu(TaskManager taskManager) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm");
        while (true) {
            System.out.println("Что вы хотите сделать?");
            System.out.println("Создать задачу -> 1");
            System.out.println("Создать эпик -> 2");
            System.out.println("Создать подзадачу -> 3");
            System.out.println("Получить задачу -> 4");
            System.out.println("Получить эпик -> 5");
            System.out.println("Получить подзадачу -> 6");
            System.out.println("Обновить задачу -> 7");
            System.out.println("Выход -> 8");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            String taskString;
            String[] taskElem;
            int taskId;
            switch (input) {
                case 1:
                    System.out.println(
                    "Введите без пробелов через запятую в формате: \n имя,описание,продолжительность," +
                            "начало(формат:\"dd.MM.yyyy;HH:mm\"");
                    taskString = scanner.next();
                    taskElem = taskString.split(",");
                    Task task = taskManager.createTask(new Task(taskElem[0], taskElem[1], Status.NEW,
                            Long.parseLong(taskElem[2]), LocalDateTime.parse(taskElem[3], formatter)));
                    if (task != null) {
                        System.out.println("Создана задача, id = " + task.getId());
                    }
                    break;
                case 2:
                    System.out.println("Введите без пробелов через запятую в формате: \n имя,описание");
                    taskString = scanner.next();
                    taskElem = taskString.split(",");
                    Epic epic = taskManager.createEpic(new Epic(taskElem[0], taskElem[1]));
                    System.out.println("Создан эпик, id = " + epic.getId());
                    break;
                case 3:
                    System.out.println(
                    "Введите без пробелов через запятую в формате: \n имя,описание,epicId,продолжительность," +
                            "начало(формат:\"dd.MM.yyyy;HH:mm\"");
                    taskString = scanner.next();
                    taskElem = taskString.split(",");
                    Subtask subtask = taskManager.createSubtask(
                            new Subtask(taskElem[0], taskElem[1], Status.NEW, Integer.parseInt(taskElem[2]),
                                    Long.parseLong(taskElem[3]), LocalDateTime.parse(taskElem[4], formatter)));
                    if (subtask != null) {
                        System.out.println("Создана подзадача, id = " + subtask.getId());
                    }
                    break;
                case 4:
                    System.out.println("Введите айди задачи:");
                    taskId = scanner.nextInt();
                    System.out.println(taskManager.getTask(taskId));
                    break;
                case 5:
                    System.out.println("Введите айди эпика:");
                    taskId = scanner.nextInt();
                    System.out.println(taskManager.getEpic(taskId));
                    break;
                case 6:
                    System.out.println("Введите айди подзадачи:");
                    taskId = scanner.nextInt();
                    System.out.println(taskManager.getSubtask(taskId));
                    break;
                case 7:
                    System.out.println("Введите без пробелов через запятую в формате: \n имя,описание,продолжительность," +
                            "начало(формат:\"dd.MM.yyyy;HH:mm\",id");
                    taskString = scanner.next();
                    taskElem = taskString.split(",");
                    taskManager.updateTask(new Task(taskElem[0], taskElem[1], Integer.parseInt(taskElem[4]), Status.NEW,
                            Long.parseLong(taskElem[2]), LocalDateTime.parse(taskElem[3], formatter)));
                case 8:
                    System.exit(0);
            }
        }

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
        System.out.println("Отсортированные задачи:");
        for (Task task: taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}
