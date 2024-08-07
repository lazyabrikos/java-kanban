package ru.yandex.javacource.novikov.schedule.manager.history;

import ru.yandex.javacource.novikov.schedule.tasks.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history;
    private Node tail;
    private Node head;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public void addTask(Task task) {
        final int id = task.getId();
        remove(id);
        linkLast(task);
        history.put(id, tail);
    }

    @Override
    public void remove(int id) {
        final Node node = history.get(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node node = new Node(task, tail, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node != null) {
            Node prev = node.prev;
            Node next = node.next;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.task = null;
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }

        return tasks;
    }

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

    }
}
