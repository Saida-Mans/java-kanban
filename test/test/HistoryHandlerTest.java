package test;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends HttpTaskServerTest {

    public HistoryHandlerTest() throws IOException {
    }

    @Test
    void shouldReturnEmptyHistoryInitially() throws Exception {
        URL url = new URL("http://localhost:8080/tasks/history");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        assertEquals(200, responseCode);

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        Task[] history = gson.fromJson(reader, Task[].class);

        assertNotNull(history);
        assertEquals(0, history.length);
    }
}