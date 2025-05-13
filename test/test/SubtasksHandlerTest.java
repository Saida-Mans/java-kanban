package test;

import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest extends HttpTaskServerTest {
    public SubtasksHandlerTest() throws IOException {
    }

    @Test
    void testCreateSubtaskReturns200AndSaves() throws Exception {
        Epic epic = new Epic("Epic 1", "desc", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 12, 0));
        int epicId = manager.createEpic(epic);
        SubTask subtask = new SubTask("Sub 1", "desc", Status.NEW, epicId, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 12, 0));
        String json = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидается 200 OK");

        List<SubTask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Должна быть 1 подзадача");
        assertEquals("Sub 1", subtasks.get(0).getName(), "Имя подзадачи не совпадает");
    }

    @Test
    void testCreateSubtaskReturnsPost200AndSaves() throws Exception {
        Epic epic = new Epic("Epic Title", "Epic Description", Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2024, 2, 1, 12, 0));
        int epicId = manager.createEpic(epic);
        SubTask subtask = new SubTask("Subtask Title", "Subtask Desc", Status.NEW, epicId,
                Duration.ofMinutes(20), LocalDateTime.of(2024, 2, 1, 12, 0));
        String json = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "POST должен вернуть 200 OK");
        List<SubTask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Должна быть 1 подзадача");
        SubTask saved = subtasks.get(0);
        assertEquals("Subtask Title", saved.getName());
        assertEquals(Status.NEW, saved.getStatus());
        assertEquals(epicId, saved.getEpicId());
    }
}
