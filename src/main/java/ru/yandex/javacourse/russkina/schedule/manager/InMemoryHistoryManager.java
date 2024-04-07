package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head = null;
    private Node<Task> tail = null;

    private final Map<Integer, Node<Task>> history;

    InMemoryHistoryManager() {
        history = new HashMap<>();
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        if (history.containsKey(id)) {
            removeNode(history.get(id));
        }
        Node<Task> newNode = new Node<>(task);
        linkLast(newNode);
        history.put(id, newNode);
    }

    public void remove(int id) {
        removeNode(history.get(id));
    }

    private void linkLast(Node<Task> task) {
        if (head == null) {
            head = task;
            tail = task;
            return;
        }
        tail.next = task;
        task.prev = tail;
        tail = task;
    }

    private List<Task> getTasks() {
        if (head == null) {
            return null;
        }
        List<Task> history = new ArrayList<>();
        Node<Task> current = head;
        while (true) {
            history.add(current.data);
            Node<Task> next = current.next;
            if (next == null) {
                break;
            }
            current = next;
        }
        return history;

    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
    }

}
