package ru.yandex.javacource.novikov.schedule.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.novikov.schedule.handlers.*;
import ru.yandex.javacource.novikov.schedule.manager.Manager;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

    }
    public static void main(String[] args) throws IOException{
        new HttpTaskServer(Manager.getInMemoryTaskManager()).start();
    }

    public void start() {
        System.out.println("Http server started port=" + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(2);
    }
}
