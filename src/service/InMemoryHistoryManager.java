package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private int size = 0;

    private Node<Task> first;

    private Node<Task> last;

    private final static Map<Integer, Node<Task>> TASK_HISTORY = new HashMap<>();

    @Override
    public void add(Task task) { // Добавление просмотра задачи в список
        if (TASK_HISTORY.containsKey(task.getId())) {
            TASK_HISTORY.remove(task.getId());
        }
        TASK_HISTORY.put(task.getId(), linkLast(task));
    }

    @Override
    public List<Task> getHistory() { // Получение списка просмотров задач
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (!TASK_HISTORY.isEmpty()) {
            TASK_HISTORY.remove(id);
            removeNode(TASK_HISTORY.get(id));
        }
    }

    private Node<Task> linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);

        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            node.data = null;
            size--;
        }
    }

    private List<Task> getTasks() {
        List<Node<Task>> nodes = new ArrayList<>(TASK_HISTORY.values());
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> taskNode : nodes) {
            tasks.add(taskNode.data);
        }
        return tasks;
    }

    private static class Node<T> {
        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}