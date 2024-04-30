package ru.yandex.javacourse.russkina.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handleGetHistory(exchange);
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
            super.sendText(exchange, gson.toJson(manager.getHistory()));
    }
}
