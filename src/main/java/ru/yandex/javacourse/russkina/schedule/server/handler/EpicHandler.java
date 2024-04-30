package ru.yandex.javacourse.russkina.schedule.server.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.russkina.schedule.exception.ManagerSaveException;
import ru.yandex.javacourse.russkina.schedule.exception.NotFoundException;
import ru.yandex.javacourse.russkina.schedule.exception.TaskValidationException;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;
import ru.yandex.javacourse.russkina.schedule.task.Epic;

import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case GET_EPIC:
                handleGetEpic(exchange);
                break;
            case POST_EPIC:
                handlePostEpic(exchange);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(exchange);
                break;
            case GET_EPIC_SUBTASKS:
                handleGetEpicSubtasks(exchange);
            default:
                super.sendBadRequest(exchange, "Такого эндпоинта не существует");
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPICS;
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_EPIC;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC;
            } else if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_EPIC;
            }
        }
        if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        return Endpoint.UNKNOWN;
    }

    enum Endpoint { GET_EPICS, GET_EPIC, GET_EPIC_SUBTASKS, DELETE_EPIC, POST_EPIC, UNKNOWN }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String epics = gson.toJson(manager.getEpics());
        super.sendText(exchange, epics);
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = TaskHandler.getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                Epic epic = manager.getEpic(taskIdOpt.get());
                String epicJson = gson.toJson(epic);
                super.sendText(exchange, epicJson);
            } catch (NotFoundException e) {
                super.sendNotFound(exchange, e.getMessage());
            } catch (ManagerSaveException e) {
                super.sendInternalServerError(exchange, e.getMessage());
            }
        } else {
            super.sendBadRequest(exchange, "Некорректный формат id");
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (body.isEmpty()) {
            sendBadRequest(exchange, "Тело запроса пустое");
        }
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic.getId() == 0) {
                manager.createEpic(epic);
                super.sendSuccess(exchange);
            }
        } catch (JsonSyntaxException e) {
            super.sendBadRequest(exchange, "Переданная задача не соответствует ожидаемому формату");
        } catch (TaskValidationException e) {
            super.sendHasInteractions(exchange, e.getMessage());
        } catch (ManagerSaveException e) {
            super.sendInternalServerError(exchange, e.getMessage());
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = TaskHandler.getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String task = gson.toJson(manager.deleteEpic(taskIdOpt.get()));
                super.sendText(exchange, task);
            } catch (NotFoundException e) {
                super.sendNotFound(exchange, e.getMessage());
            } catch (ManagerSaveException e) {
                super.sendInternalServerError(exchange, e.getMessage());
            }
        } else {
            super.sendBadRequest(exchange, "Некорректный формат id");
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = TaskHandler.getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String epicSubtasks = gson.toJson(manager.getEpicSubtasks(taskIdOpt.get()));
                super.sendText(exchange, epicSubtasks);
            } catch (NotFoundException e) {
                super.sendNotFound(exchange, e.getMessage());
            }
        } else {
            super.sendBadRequest(exchange, "Некорректный формат id");
        }
    }
}