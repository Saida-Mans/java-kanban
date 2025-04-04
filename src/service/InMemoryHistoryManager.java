package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> table = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private void linkLast(Task task) {
        Node<Task> element = new Node();
        element.setTask(task);
        if (table.containsKey(task.getId())) {
            removeNode(table.get(task.getId()));
        }
        if (head == null) {
            tail = element;
            head = element;
            element.setNext(null);
            element.setPrev(null);
        } else {
            element.setPrev(tail);
            element.setNext(null);
            tail.setNext(element);
            tail = element;
        }
        table.put(task.getId(), element);
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node<Task> element = head;
        while (element != null) {
            result.add(element.getTask());
            element = element.getNext();
        }
        return result;
    }

    private void removeNode(Node node) {
        if (node != null) {
            table.remove(node.getTask().getId());
            Node<Task> prev = node.getPrev();
            Node<Task> next = node.getNext();

            if (head == node) {
                head = node.getNext();
            }
            if (tail == node) {
                tail = node.getPrev();
            }
            if (prev != null) {
                prev.setNext(next);
            }
            if (next != null) {
                next.setPrev(prev);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (table.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = table.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }
}

