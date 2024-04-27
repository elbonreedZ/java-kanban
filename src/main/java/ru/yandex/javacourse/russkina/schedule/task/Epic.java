package ru.yandex.javacourse.russkina.schedule.task;

import ru.yandex.javacourse.russkina.schedule.manager.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private static final Type TYPE = Type.EPIC;
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private List<Integer> subtasksId;

    @Override
    public Type getType() {
        return TYPE;
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
        subtasksId = new ArrayList<>();
    }
}
