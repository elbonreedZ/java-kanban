package ru.yandex.javacourse.russkina.schedule.task;

import ru.yandex.javacourse.russkina.schedule.manager.Type;

import java.util.ArrayList;

public class Epic extends Task {

    private static final Type TYPE = Type.EPIC;

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
}
