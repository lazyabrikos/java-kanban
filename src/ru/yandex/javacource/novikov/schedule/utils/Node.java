package ru.yandex.javacource.novikov.schedule.utils;

import ru.yandex.javacource.novikov.schedule.tasks.Task;

public  class Node {
    private Task task;
    private Node prev;
    private Node next;


    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getPrev() {
        return this.prev;
    }
}