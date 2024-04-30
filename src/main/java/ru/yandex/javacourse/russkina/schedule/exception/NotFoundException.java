package ru.yandex.javacourse.russkina.schedule.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}