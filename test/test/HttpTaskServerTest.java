package test;

import com.google.gson.Gson;
import http.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    HistoryManager history;
    TaskManager manager;
    HttpTaskServer server;
    Gson gson;

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
        public void setUp() throws Exception {
           history=Managers.getDefaultHistory();
            manager = new InMemoryTaskManager(history);
            server = new HttpTaskServer(manager);
            server.start();
            gson = HttpTaskServer.getGson();;
        }

    @AfterEach
    public void tearDown() {
        server.stop();
    }
}