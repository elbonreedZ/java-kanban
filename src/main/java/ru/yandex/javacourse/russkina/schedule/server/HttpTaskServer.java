package ru.yandex.javacourse.russkina.schedule.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.russkina.schedule.manager.Managers;
import ru.yandex.javacourse.russkina.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {

    TaskManager manager;
    private static final int PORT = 8080;

    HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

    public HttpTaskServer() throws IOException {
    }


    public void start() throws IOException {
        manager = Managers.getDefault();
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
        return BaseHttpHandler.gson;
    }

    public static void main(String[] args) throws IOException {
    }

    public void stop() {
        httpServer.stop(1);
    }
}
