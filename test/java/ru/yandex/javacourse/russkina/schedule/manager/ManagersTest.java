package ru.yandex.javacourse.russkina.schedule.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

    @Test
    public void shouldReturnTaskManagerWhenManagersWorks() {
        assertNotNull(taskManager);
    }

    @Test
    public void shouldReturnHistoryManagerWhenManagersWorks() {
        assertNotNull(historyManager);
    }

}