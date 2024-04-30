package ru.yandex.javacourse.russkina.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.russkina.schedule.manager.Managers;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;
import ru.yandex.javacourse.russkina.schedule.server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;


public class HttpTaskServer {

    private static final int PORT = 8080;

    private final TaskManager manager = Managers.getDefault();

    private final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    public HttpTaskServer() throws IOException {
    }


    public void start() throws IOException {
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizeHandler(manager));
        httpServer.start();
    }

    public TaskManager getManager() {
        return manager;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {
    }

    public void stop() {
        httpServer.stop(1);
    }
}
