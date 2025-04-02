package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
            private final Map<Integer, Node> table = new HashMap<>();
            private Node head;
            private Node tail;
            private int size = 0;

            private void linkLast(Task task) {
                Node element = new Node();
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
                Node element = head;
                while (element != null) {
                    result.add(element.getTask());
                }
                return result;
            }

            private void removeNode(Node node) {
                if (node != null) {
                    table.remove(node.getTask().getId());
                    Node prev = node.getPrev();
                    Node next = node.getNext();

                    if (head == node) {
                        head = node.getNext();
                    }
                    if (tail == node) {
                        tail = node.getPrev();
                    }
                }
            }
            @Override
            public List<Task> getHistory() {
                List<Task> history = new ArrayList<>();
             Node node = head;
             while (node != null) {
            history.add(node.getTask());
            node = node.getNext();
             }
            return history;
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
        Node node = table.remove(id);
        if (node != null) {
            removeNode(node);
         }
        }
       }

