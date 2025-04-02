package Test;

import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.util.HashMap;
import java.util.Map;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    void removeHistory(){
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        Task task1=new Task("Задача", "Описание-1", NEW);
        historyManager.add(task1);
        Task task2=new Task("Задача", "Описание-1", NEW);
        historyManager.add(task2);
        Task task3=new Task("Задача", "Описание-1", NEW);
        historyManager.add(task3);
        int taskId = taskManager.createTask(task1);
        historyManager.remove(taskId);
    }








}