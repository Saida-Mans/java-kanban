package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import model.Task;
import service.IntersectionException;
import service.NotFoundException;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (path.equals("/subtasks")) {
                switch (method) {
                    case GET:
                        List<Task> tasks = manager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks));
                        break;
                    case POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        if (subTask.getId() != 0 && manager.getSubtaskById(subTask.getId()) != null) {
                            manager.updateSubtask(subTask);
                        } else {
                            manager.createSubtask(subTask);
                        }
                        sendText(exchange, gson.toJson(subTask));
                        break;
                    default:
                        sendMethodNotAllowed(exchange);
                }
            } else if (path.matches("^/subtasks/\\d+$")) {
                int id = extractIdFromPath(path);
                switch (method) {
                    case "GET":
                        try {
                            SubTask subtask = manager.getSubtaskById(id);
                            sendText(exchange, gson.toJson(subtask));
                        } catch (NotFoundException e) {
                            sendNotFound(exchange);  // Если задача не найдена
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
}