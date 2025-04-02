package test;

import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

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
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }



    @Test
    void remove(){
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История пустая.");
    }
    }