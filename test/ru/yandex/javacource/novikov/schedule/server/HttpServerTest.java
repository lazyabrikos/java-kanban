package ru.yandex.javacource.novikov.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.yandex.javacource.novikov.schedule.adapters.DurationAdapter;
import ru.yandex.javacource.novikov.schedule.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.novikov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.novikov.schedule.manager.TaskManager;
import ru.yandex.javacource.novikov.schedule.tasks.Epic;
import ru.yandex.javacource.novikov.schedule.tasks.Status;
import ru.yandex.javacource.novikov.schedule.tasks.Subtask;
import ru.yandex.javacource.novikov.schedule.tasks.Task;
import ru.yandex.javacource.novikov.schedule.tokens.EpicListToken;
import ru.yandex.javacource.novikov.schedule.tokens.SubtaskListToken;
import ru.yandex.javacource.novikov.schedule.tokens.TaskListToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class HttpServerTest {
    protected Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    ;
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    protected HttpClient httpClient;

    public HttpServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        taskManager.removeAllSubtasks();
        taskServer.start();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );

        Task task2 = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 30))
        );


        String taskJson = gson.toJson(task);
        String taskJson2 = gson.toJson(task2);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, postResponse.statusCode());
        HttpResponse<String> postResponse2 = httpClient.send(postRequest2, HttpResponse.BodyHandlers.ofString());
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, getResponse.statusCode());
        List<Task> tasks = gson.fromJson(getResponse.body(), new TaskListToken().getType());
        Assertions.assertEquals(2, tasks.size(), "No task in manager");
    }

    @Test
    public void testIntersection() throws IOException, InterruptedException {
        Task task = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );

        Task task2 = new Task("Task2",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );

        String taskJson = gson.toJson(task);
        String taskJson2 = gson.toJson(task2);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, postResponse.statusCode());
        HttpResponse<String> postResponse2 = httpClient.send(postRequest2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, postResponse2.statusCode());
    }

    @Test
    public void testFindById() throws IOException, InterruptedException {
        Task task = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Task taskFromRequest = gson.fromJson(getResponse.body(), Task.class);
        Assertions.assertEquals(200, getResponse.statusCode());
        Assertions.assertNotNull(taskFromRequest);
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, deleteResponse.statusCode());
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, getResponse.statusCode());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Task",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        task = taskManager.getTask(1);
        task.setStatus(Status.DONE);
        String updatedTaskJson = gson.toJson(task);
        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> updateResponse = httpClient.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, updateResponse.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Task updatedTask = gson.fromJson(getResponse.body(), Task.class);
        Assertions.assertEquals(Status.DONE, updatedTask.getStatus(), "Status not equal");
    }


    @Test
    public void testAddEpicAndGetIt() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "test");
        URI url = URI.create("http://localhost:8080/epics");

        HttpClient httpClient = HttpClient.newHttpClient();

        String jsonEpic = gson.toJson(epic);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, postResponse.statusCode());

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, getResponse.statusCode());
        List<Epic> epics = gson.fromJson(getResponse.body(), new EpicListToken().getType());
        Assertions.assertEquals(1, epics.size(), "Sizes not equal");


        HttpRequest getRequestById = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .build();
        HttpResponse<String> getResponseById = httpClient.send(getRequestById, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, getResponseById.statusCode());
        Epic getEpic = gson.fromJson(getResponseById.body(), Epic.class);
        Assertions.assertNotNull(getEpic);
    }

    @Test
    public void addEpicAnd2SubtasksAndTryToGetEpicsSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "test");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );

        Subtask subtask2 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 30))
        );

        URI url = URI.create("http://localhost:8080/subtasks");
        String subtask1Json = gson.toJson(subtask1);
        String subtask2Json = gson.toJson(subtask2);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest postFirstSubtask = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();


        HttpRequest postSecondSubtask = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask2Json))
                .build();

        HttpResponse<String> firstResponse = httpClient.send(postFirstSubtask, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> secondResponse = httpClient.send(postSecondSubtask, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, firstResponse.statusCode());
        Assertions.assertEquals(201, secondResponse.statusCode());

        HttpRequest getEpicSubtasksRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> getEpicSubtasksResponse = httpClient.send(
                getEpicSubtasksRequest,
                HttpResponse.BodyHandlers.ofString()
        );
        Assertions.assertEquals(200, getEpicSubtasksResponse.statusCode());
        List<Subtask> subtasks = gson.fromJson(getEpicSubtasksResponse.body(), new SubtaskListToken().getType());
        Assertions.assertEquals(2, subtasks.size(), "Size not equal");
    }

    @Test
    public void testIfSubtaskStatusUpdatedThenUpdatedEpicStatus() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "test");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );

        Subtask subtask2 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 30))
        );
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);

        subtask1.setStatus(Status.IN_PROGRESS);
        String subtask1Json = gson.toJson(subtask1);

        HttpRequest postSubtask = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();
        HttpResponse<String> postResponse = httpClient.send(postSubtask, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, postResponse.statusCode());
        HttpRequest getEpicRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .build();
        HttpResponse<String> getEpicResponse = httpClient.send(getEpicRequest, HttpResponse.BodyHandlers.ofString());
        Epic getEpic = gson.fromJson(getEpicResponse.body(), Epic.class);
        Assertions.assertEquals(Status.IN_PROGRESS, getEpic.getStatus(), "Status not equal");
    }

    @Test
    public void testNotExistingPath() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/somethinwentwrong"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getPrioritized() throws IOException, InterruptedException {

        Task task1 = new Task("Task1",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        Task task2 = new Task("Task2",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30))
        );

        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);

        Epic epic = new Epic("Test epic", "test");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 30))
        );

        Subtask subtask2 = new Subtask("subtask1",
                "test_subtask",
                epicId,
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 30))
        );
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        List<Task> prioritized = gson.fromJson(response.body(), new TaskListToken().getType());
        Assertions.assertEquals(3, prioritized.size());
    }


    @Test
    public void getHistory() throws IOException, InterruptedException {

        Task task1 = new Task("Task1",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30))
        );
        Task task2 = new Task("Task2",
                "Descr",
                Duration.ofMinutes(30),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30))
        );

        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);

        Task getTask1 = taskManager.getTask(task1Id);
        Task getTask2 = taskManager.getTask(task2Id);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        List<Task> history = gson.fromJson(response.body(), new TaskListToken().getType());
        Assertions.assertEquals(2, history.size());
    }
}