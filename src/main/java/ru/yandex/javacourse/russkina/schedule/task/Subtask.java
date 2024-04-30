package ru.yandex.javacourse.russkina.schedule.task;
import ru.yandex.javacourse.russkina.schedule.manager.Type;
import java.time.LocalDateTime;

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

    public Subtask(String name, String description, Status status, int epicId, long duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, int epicId, Status status,
                   long duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
