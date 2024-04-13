package ru.yandex.javacourse.russkina.schedule.task;

import ru.yandex.javacourse.russkina.schedule.manager.Types;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId;

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
        subtasksId = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s", this.getId(), Types.EPIC, this.getName(), this.getStatus(),
                this.getDescription());
    }
}
