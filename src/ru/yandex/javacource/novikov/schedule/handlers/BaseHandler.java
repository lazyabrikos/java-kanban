package ru.yandex.javacource.novikov.schedule.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.novikov.schedule.adapters.DurationAdapter;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.adapters.LocalDateTimeAdapter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    protected BaseHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    }

    protected void sendText(HttpExchange httpExchange, String text, int rCode) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        httpExchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }

    }

    protected void sendNotFound(HttpExchange httpExchange, String text) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(404, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void sendIntersections(HttpExchange httpExchange, String text) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(406, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void sendError(HttpExchange httpExchange, String text, int rCode) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }
}
