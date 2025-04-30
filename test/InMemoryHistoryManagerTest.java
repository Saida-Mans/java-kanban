package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void newHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер не проинициализирован");
    }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1= new Task("Test Task", "Test Description", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 1, 10, 0));
        historyManager.add(task1);
        assertEquals(List.of(task1), historyManager.getHistory());
    }

    @Test
    void remove() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История пустая.");
    }
}