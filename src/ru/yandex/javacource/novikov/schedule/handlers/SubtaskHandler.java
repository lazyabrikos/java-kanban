package ru.yandex.javacource.novikov.schedule.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.novikov.schedule.exceptions.NoFoundException;
import ru.yandex.javacource.novikov.schedule.exceptions.ValidationException;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().toString();
        String method = httpExchange.getRequestMethod();
        String[] splitPath = path.split("/");
        switch (method) {
            case "GET": {
                if (path.equals("/subtasks")) {
                    List<Subtask> subtasks = taskManager.getAllSubtasks();
                    String jsonString = gson.toJson(subtasks);
                    sendText(httpExchange, jsonString, 200);
                } else if (Pattern.matches("^/subtasks/\\d+", path)) {
                    int subtaskId = Integer.parseInt(splitPath[2]);
                    try {
                        Subtask subtask = taskManager.getSubtask(subtaskId);
                        String jsonString = gson.toJson(subtask);
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
                if (path.equals("/subtasks")) {
                    String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    try {

                        Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
                        System.out.println(subtask.toString());
                        int subtaskId = subtask.getId();
                        if (subtaskId == 0) {
                            try {
                                subtaskId = taskManager.addSubtask(subtask);
                                sendText(httpExchange, "Subtask added with id=" + subtaskId, 201);
                            } catch (ValidationException e) {
                                sendIntersections(httpExchange,
                                        "Cannot add subtask because of intersection with another task"
                                );
                            } catch (NoFoundException e) {
                                sendNotFound(httpExchange, e.getMessage());
                            }
                        } else {
                            try {
                                taskManager.updateSubtask(subtask);
                                sendText(httpExchange,
                                        String.format("Subtask with id=%d successfully updates", subtaskId),
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
                if (Pattern.matches("^/subtasks/\\d+", path)) {
                    int subtaskId = Integer.parseInt(splitPath[2]);
                    taskManager.removeSubtask(subtaskId);
                    sendText(httpExchange,
                            String.format("Subtask with id=%d deleted", subtaskId),
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
