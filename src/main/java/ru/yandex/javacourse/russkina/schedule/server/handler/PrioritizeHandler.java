package ru.yandex.javacourse.russkina.schedule.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;

import java.io.IOException;

public class PrioritizeHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    public PrioritizeHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handleGetPrioritizedTasks(exchange);
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        super.sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
    }
}