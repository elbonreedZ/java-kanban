package ru.yandex.javacourse.russkina.schedule.exception;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
}

