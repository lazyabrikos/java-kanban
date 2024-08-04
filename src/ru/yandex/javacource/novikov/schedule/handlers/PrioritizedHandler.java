package ru.yandex.javacource.novikov.schedule.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHandler {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().toString();
        String method = httpExchange.getRequestMethod();
        String[] splitPath = path.split("/");
        if (method.equals("GET")) {
            if (path.equals("/prioritized")) {
                List<Task> tasks = taskManager.getPrioritizedTasks();
                String jsonString = gson.toJson(tasks);
                sendText(httpExchange, jsonString, 200);
            } else {
                sendNotFound(httpExchange, "Not found path");
            }
        } else {
            sendError(httpExchange, "Method not allowed", 405);
        }
    }
}
