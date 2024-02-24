package ru.yandex.javacourse.russkina.schedule.manager;
import ru.yandex.javacourse.russkina.schedule.task.*;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    ArrayList<Task>  getHistory();

}
