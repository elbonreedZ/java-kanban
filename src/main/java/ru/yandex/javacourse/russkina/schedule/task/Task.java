package ru.yandex.javacourse.russkina.schedule.task;

import ru.yandex.javacourse.russkina.schedule.manager.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private static final Type TYPE = Type.TASK;
    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int id, Status status, long minutes, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = Duration.ofMinutes(minutes);
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, long minutes, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(minutes);
        this.startTime = startTime;
    }

    public Long getDuration() {
        if (duration == null) {
            return 0L;
        }
        return duration.toMinutes();
    }

    public void setDuration(Long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return this.startTime.plus(duration);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return (id == otherTask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("id = %d, %s, %s, %s, %d, %s, %s", this.getId(), this.getName(), this.getStatus(),
                this.getDescription(), this.getDuration(), this.getStartTime(), this.getEndTime());
    }
}
