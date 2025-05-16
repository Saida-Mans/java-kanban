package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.IntersectionException;
import service.NotFoundException;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (path.equals("/tasks")) {
                switch (method) {
                    case GET:
                        List<Task> tasks = manager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks));
                        break;
                    case POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        if (task.getId() != 0 && manager.getTaskById(task.getId()) != null) {
                            manager.updateTask(task);
                        } else {
                            manager.createTask(task);
                        }
                        sendText(exchange, "OK");
                        break;
                    default:
                        sendMethodNotAllowed(exchange);
                }
            } else if (path.matches("^/tasks/\\d+$")) {
                int id = extractIdFromPath(path);
                switch (method) {
                    case GET:
                        try {
                            Task task = manager.getTaskById(id);
                            sendText(exchange, gson.toJson(task));
                        } catch (NotFoundException e) {
                            sendNotFound(exchange);
                        }
                        break;
                    case DELETE:
                        manager.deleteTaskById(id);
                        sendText(exchange, "Deleted");
                        break;
                    default:
                        sendMethodNotAllowed(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (IntersectionException e) {
            sendConflict(exchange);
        } catch (Exception e) {
            e.printStackTrace();
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
}