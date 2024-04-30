package ru.yandex.javacourse.russkina.schedule.server;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.russkina.schedule.exception.ManagerSaveException;
import ru.yandex.javacourse.russkina.schedule.exception.NotFoundException;
import ru.yandex.javacourse.russkina.schedule.exception.TaskValidationException;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;
import ru.yandex.javacourse.russkina.schedule.task.Task;
import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case GET_TASK:
                handleGetTask(exchange);
                break;
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTask(exchange);
                break;
            default:
                super.sendBadRequest(exchange, "Такого эндпоинта не существует");
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK;
            } else if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
        }
        return Endpoint.UNKNOWN;
    }

    enum Endpoint { GET_TASKS, GET_TASK, POST_TASK, DELETE_TASK, UNKNOWN }

    public static Optional<Integer> getTaskId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String idString = path.split("/")[2];
        try {
            Integer id = Integer.parseInt(idString);
            return Optional.of(id);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String tasks = gson.toJson(manager.getTasks());
        super.sendText(exchange, tasks);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String task = gson.toJson(manager.getTask(taskIdOpt.get()));
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

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        if (body.isEmpty()) {
            sendBadRequest(exchange, "Тело запроса пустое");
        }
        try {
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() == 0) {
                manager.createTask(task);
                super.sendSuccess(exchange);
            } else {
                manager.updateTask(task);
                super.sendSuccess(exchange);
            }
        } catch (JsonSyntaxException e) {
            super.sendBadRequest(exchange, "Переданная задача не соответствует ожидаемому формату");
        } catch (TaskValidationException e) {
            super.sendHasInteractions(exchange, e.getMessage());
        } catch (NotFoundException e) {
            super.sendNotFound(exchange,e.getMessage());
        } catch (ManagerSaveException e) {
            super.sendInternalServerError(exchange, e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String task = gson.toJson(manager.deleteTask(taskIdOpt.get()));
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
}
