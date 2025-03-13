package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history=new ArrayList<>();
    private int maxHistorySize=10;

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= maxHistorySize) {
                history.remove(0);
                history.add(task);
            } else {
                history.add(task);
            }
        }
    }
}

