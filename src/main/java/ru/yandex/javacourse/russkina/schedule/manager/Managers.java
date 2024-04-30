package ru.yandex.javacourse.russkina.schedule.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return getInMemoryTaskManager();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }
}
