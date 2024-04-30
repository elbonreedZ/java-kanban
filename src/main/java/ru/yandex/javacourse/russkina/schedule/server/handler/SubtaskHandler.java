package ru.yandex.javacourse.russkina.schedule.server.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.russkina.schedule.exception.ManagerSaveException;
import ru.yandex.javacourse.russkina.schedule.exception.NotFoundException;
import ru.yandex.javacourse.russkina.schedule.exception.TaskValidationException;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskHandler.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
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

    private TaskHandler.Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return TaskHandler.Endpoint.GET_TASKS;
            } else if (requestMethod.equals("POST")) {
                return TaskHandler.Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return TaskHandler.Endpoint.GET_TASK;
            } else if (requestMethod.equals("DELETE")) {
                return TaskHandler.Endpoint.DELETE_TASK;
            }
        }
        return TaskHandler.Endpoint.UNKNOWN;
    }


    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String tasks = gson.toJson(manager.getSubtasks());
        super.sendText(exchange, tasks);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = TaskHandler.getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String task = gson.toJson(manager.getSubtask(taskIdOpt.get()));
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
            Subtask task = gson.fromJson(body, Subtask.class);
            if (task.getId() == 0) {
                manager.createSubtask(task);
                super.sendSuccess(exchange);
            } else {
                manager.updateSubtask(task);
                super.sendSuccess(exchange);
            }
        } catch (JsonSyntaxException e) {
            super.sendBadRequest(exchange, "Переданная задача не соответствует ожидаемому формату");
        } catch (TaskValidationException e) {
            super.sendHasInteractions(exchange, e.getMessage());
        } catch (NotFoundException e) {
            super.sendNotFound(exchange, e.getMessage());
        } catch (ManagerSaveException e) {
            super.sendInternalServerError(exchange, e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = TaskHandler.getTaskId(exchange);
        if (taskIdOpt.isPresent()) {
            try {
                String task = gson.toJson(manager.deleteSubtask(taskIdOpt.get()));
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
