package test;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;
import test.HttpTaskServerTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest extends HttpTaskServerTest {

    public EpicsHandlerTest() throws IOException {
    }

    @Test
    void testGetAllEpicsReturns200AndCorrectList() throws Exception {
        Epic epic1 =  new Epic("Epic 1", "desc", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 12, 0));
        Epic epic2 =  new Epic("Epic 2", "desc", Status.NEW, Duration.ofMinutes(35), LocalDateTime.of(2025, 1, 1, 12, 0));
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "GET /epics должен вернуть 200");
        Type epicListType = new TypeToken<List<Epic>>() {}.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicListType);
        assertEquals(2, epics.size(), "Должно быть 2 эпика");
    }
}