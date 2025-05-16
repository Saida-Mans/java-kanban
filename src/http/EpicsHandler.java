package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("/epics".equals(path) && GET.equals(method)) {
                List<Epic> epics = manager.getAllEpics();
                sendText(exchange, gson.toJson(epics));
                return;
            }
            if (path.matches("^/epics/\\d+$")) {
                int id = extractIdFromPath(path);
                if (GET.equals(method)) {
                    Epic epic = manager.getEpicById(id);
                    if (epic != null) {
                        sendText(exchange, gson.toJson(epic));
                    } else {
                        sendNotFound(exchange);
                    }
                } else if (POST.equals(method)) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic newEpic = gson.fromJson(body, Epic.class);
                    manager.createEpic(newEpic);
                    sendCreated(exchange, "Created");

                } else if (DELETE.equals(method)) {
                    if (manager.getEpicById(id) != null) {
                        manager.deleteEpicById(id);
                        sendText(exchange, "Deleted");
                    } else {
                        sendNotFound(exchange);
                    }
                } else {
                    sendNotFound(exchange);
                }
                return;
            }

            if (path.matches("^/epics/\\d+/subtasks$") && GET.equals(method)) {
                int id = extractIdFromPath(path);
                Epic epic = manager.getEpicById(id);
                if (epic == null) {
                    sendNotFound(exchange);
                } else {
                    List<SubTask> subTasks = manager.getAllSubtasksEpic(id);
                    sendText(exchange, gson.toJson(subTasks));
                }
                return;
            }
            sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
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