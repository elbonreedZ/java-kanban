package ru.yandex.javacourse.russkina.schedule.task;

import ru.yandex.javacourse.russkina.schedule.manager.Type;

public class Subtask extends Task {

    private static final Type TYPE = Type.SUBTASK;
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, int epicId, Status status) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
