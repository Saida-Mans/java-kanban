package http;

import adapters.GsonFactory;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected static final Gson gson = GsonFactory.getGson();

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendResponse(h, text, 200);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        sendResponse(h, text, 201);
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendResponse(h, "{\"error\":\"Not found\"}", 404);
    }

    protected void sendConflict(HttpExchange h) throws IOException {
        sendResponse(h, "{\"error\":\"Task intersects with existing task\"}", 406);
    }

    protected void sendMethodNotAllowed(HttpExchange h) throws IOException {
        sendResponse(h, "{\"error\":\"Method not allowed\"}", 405);
    }

    protected void sendServerError(HttpExchange h) throws IOException {
        sendResponse(h, "{\"error\":\"Internal server error\"}", 500);
    }

    private void sendResponse(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }
}