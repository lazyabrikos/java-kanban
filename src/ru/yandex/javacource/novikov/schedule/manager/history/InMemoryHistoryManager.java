package ru.yandex.javacource.novikov.schedule.manager.history;

import ru.yandex.javacource.novikov.schedule.tasks.*;
import ru.yandex.javacource.novikov.schedule.utils.Node;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
//Делал по ТЗ, но не логичней будет вынести реализацию двусвязного списка в отдельный класс, как сделал для Node?

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodesByIds;
    private Node tail;
    private Node head;

    public InMemoryHistoryManager() {
        nodesByIds = new HashMap<>();
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if (nodesByIds.containsKey(task.getId())) {
            removeNode(nodesByIds.get(task.getId()));
        }

        Node oldTail = tail;
        Node node = new Node(task, tail, null);
        tail = node;
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.setNext(node);
        }

        nodesByIds.put(task.getId(), node);

    }

    @Override
    public void remove(int id) {
        if (nodesByIds.containsKey(id)) {
            removeNode(nodesByIds.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node node) {
        if (node != null) {
            nodesByIds.remove(node.getTask().getId());
            Node prev = node.getPrev();
            Node next = node.getNext();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null){
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            node.setTask(null);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.getTask());
            node = node.getNext();
        }

        return tasks;
    }
}
