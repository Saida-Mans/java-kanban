package http;

import adapters.GsonFactory;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson =  GsonFactory.getGson();

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (path.equals("/epics")) {
                // /epics — GET getEpics() -> 200
                if ("GET".equals(method)) {
                    List<Epic> epics = manager.getAllEpics();
                    sendText(exchange, gson.toJson(epics), 200);
                } else {
                    sendMethodNotAllowed(exchange);
                }

            } else if (path.matches("^/epics/\\d+$")) {
                int id = extractIdFromPath(path);
                switch (method) {
                    case "GET": {
                        Epic epic = manager.getEpicById(id);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                        break;
                    }
                    case "POST": {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Epic newEpic = gson.fromJson(body, Epic.class);
                        manager.createEpic(newEpic);
                        sendText(exchange, "Created", 201);
                        break;
                    }
                    case "DELETE": {
                        if (manager.getEpicById(id) != null) {
                            manager.deleteEpicById(id);
                            sendText(exchange, "Deleted", 200);
                        } else {
                            sendNotFound(exchange);
                        }
                        break;
                    }
                    default:
                        sendMethodNotAllowed(exchange);
                }
            } else if (path.matches("^/epics/\\d+/subtasks$")) {
                int id = extractIdFromPath(path);
                if ("GET".equals(method)) {
                    Epic epic = manager.getEpicById(id);
                    if (epic == null) {
                        sendNotFound(exchange);
                    } else {
                        List<SubTask> subTasks = manager.getAllSubtasksEpic(id);
                        sendText(exchange, gson.toJson(subTasks), 200);
                    }
                } else {
                    sendMethodNotAllowed(exchange);
                }

            } else {
                sendNotFound(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendServerError(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[2]);
        } catch (Exception e) {
            return -1;
        }
    }

    protected void sendText(HttpExchange exchange, String response) throws IOException {
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendText(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "{\"error\":\"Epic not found\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, 0);
        exchange.close();
    }

    protected void sendServerError(HttpExchange exchange) throws IOException {
        String response = "{\"error\":\"Internal server error\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}