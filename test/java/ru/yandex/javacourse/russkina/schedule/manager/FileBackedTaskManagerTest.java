package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.russkina.schedule.exception.TaskValidationException;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Status;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.io.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    static TaskManager taskManager;
    static Task task;
    static Epic epic;
    static Subtask subtask;

    static File file;

    @BeforeAll
    public static void beforeAll() {
        try {
            file = File.createTempFile("java-kanban-file", ".cvs");
            taskManager = FileBackedTaskManager.loadFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        task = taskManager.createTask(new Task("name", "description", Status.NEW,
                180, LocalDateTime.of(2024, 2, 3, 14,40)));
        epic = taskManager.createEpic(new Epic("name", "description"));
        subtask = taskManager.createSubtask(
                new Subtask("name", "description", Status.NEW, epic.getId(),
                        180, LocalDateTime.of(2023, 2, 3, 14,40)));
    }

    @Test
    public void shouldReturnStringTaskInSecondLine() throws IOException {
        String taskInManager = CVSTaskFormat.toString(task);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String taskInFile = br.readLine();
            assertEquals(taskInManager, taskInFile);
        }
    }

    @Test
    public void shouldReturnStringHistoryAfterEmptyLine() throws IOException {
        taskManager.getTask(1);
        String history = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            boolean isHistory = false;
            while (br.ready()) {
                String line = br.readLine();
                if (line.isBlank()) {
                    isHistory = true;
                    continue;
                }
                if (isHistory) {
                    history = line;
                }
            }
            assertEquals(history, "" + task.getId());
        }
    }
    //я не могу сделать тесты на исключения, так как исключения обрабатываются в самом методе
}