package test;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest extends HttpTaskServerTest {

    public PrioritizedHandlerTest() throws IOException {
    }

    @Test
    void shouldReturnEmptyPrioritizedListInitially() throws IOException {
        URL url = new URL("http://localhost:8080/tasks/prioritized");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200");

        String responseBody = new String(connection.getInputStream().readAllBytes());
        Task[] tasks = gson.fromJson(responseBody, Task[].class);

        assertNotNull(tasks, "Response should not be null");
        assertEquals(0, tasks.length, "Initially, prioritized tasks list should be empty");
    }
}