package ru.yandex.javacource.novikov.schedule.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.novikov.schedule.exceptions.NoFoundException;
import ru.yandex.javacource.novikov.schedule.exceptions.ValidationException;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().toString();
        String method = httpExchange.getRequestMethod();
        String[] splitPath = path.split("/");
        switch (method) {
            case "GET": {
                if (path.equals("/epics")) {
                    List<Epic> epics = taskManager.getAllEpics();
                    try {
                        String jsonString = gson.toJson(epics);
                        sendText(httpExchange, jsonString, 200);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else if (Pattern.matches("^/epics/\\d+", path)) {
                    int epicId = Integer.parseInt(splitPath[2]);
                    try {
                        Epic epic = taskManager.getEpic(epicId);
                        String jsonString = gson.toJson(epic);
                        sendText(httpExchange, jsonString, 200);
                    } catch (NoFoundException e) {
                        sendNotFound(httpExchange, e.getMessage());

                    }
                } else if (Pattern.matches("^/epics/\\d+/subtasks", path)) {
                    int epicId = Integer.parseInt(splitPath[2]);
                    try {
                        List<Subtask> subtaskList = taskManager.getAllEpicSubtasks(epicId);
                        String jsonString = gson.toJson(subtaskList);
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
                if (path.equals("/epics")) {
                    String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    try {
                        Epic epic = gson.fromJson(jsonElement, Epic.class);
                        int epicId = epic.getId();
                        if (epicId == 0) {
                            try {
                                epicId = taskManager.addEpic(epic);
                                sendText(httpExchange, "Epic added with id=" + epicId, 201);
                            } catch (ValidationException e) {
                                sendIntersections(httpExchange,
                                        "Cannot add epic because of intersection with another task"
                                );
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        sendError(httpExchange,
                                "Error in request",
                                400
                        );
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    sendNotFound(httpExchange, "Not found path");
                }
                break;
            }
            case "DELETE": {
                if (Pattern.matches("^/epics/\\d+", path)) {
                    int epicId = Integer.parseInt(splitPath[2]);
                    taskManager.removeEpic(epicId);
                    sendText(httpExchange,
                            String.format("Epic with id=%d deleted", epicId),
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
