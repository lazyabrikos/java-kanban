package ru.yandex.javacource.novikov.schedule.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.novikov.schedule.exceptions.NoFoundException;
import ru.yandex.javacource.novikov.schedule.exceptions.ValidationException;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().toString();
        String method = httpExchange.getRequestMethod();
        String[] splitPath = path.split("/");
        switch (method) {
            case "GET": {
                if (path.equals("/tasks")) {
                    List<Task> tasks = taskManager.getAllTasks();
                    String jsonString = gson.toJson(tasks);
                    sendText(httpExchange, jsonString, 200);
                } else if (Pattern.matches("^/tasks/\\d+", path)) {
                    int taskId = Integer.parseInt(splitPath[2]);
                    try {
                        Task task = taskManager.getTask(taskId);
                        String jsonString = gson.toJson(task);
                        sendText(httpExchange, jsonString, 200);
                    } catch (NoFoundException e) {
                        sendNotFound(httpExchange, e.getMessage());
                    }
                } else {
                    sendNotFound(httpExchange, "Not found path");
                }
                break;
            }
            case "POST": {
                if (path.equals("/tasks")) {
                    String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    try {
                        Task task = gson.fromJson(jsonElement, Task.class);
                        int taskId = task.getId();
                        if (taskId == 0) {
                            try {
                                taskId = taskManager.addTask(task);
                                sendText(httpExchange, "Task added with id=" + taskId, 201);
                            } catch (ValidationException e) {
                                sendIntersections(httpExchange,
                                        "Cannot add task because of intersection with another task"
                                );
                            }
                        } else {
                            try {
                                taskManager.updateTask(task);
                                sendText(httpExchange,
                                        String.format("Task with id=%d successfully updates", taskId),
                                        201
                                );
                            } catch (NoFoundException e) {
                                sendNotFound(httpExchange, e.getMessage());
                            }


                        }
                    } catch (JsonSyntaxException e) {
                        sendError(httpExchange,
                                "Error in request",
                                400
                        );
                    }
                } else {
                    sendNotFound(httpExchange, "Not found path");
                }
                break;
            }
            case "DELETE": {
                if (Pattern.matches("^/tasks/\\d+", path)) {
                    int taskId = Integer.parseInt(splitPath[2]);
                    taskManager.removeTask(taskId);
                    sendText(httpExchange,
                            String.format("Task with id=%d deleted", taskId),
                            200
                    );
                }
                break;
            }
            default: {
                sendError(httpExchange, "Method not allowed", 405);
            }
        }
    }
}
