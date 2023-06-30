package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static int size = 0;

    private static Node<Task> first;

    private static Node<Task> last;

    private final static Map<Integer, Node<Task>> TASK_HISTORY = new HashMap<>();

    private final static List<Node<Task>> NODES = new ArrayList<>();

    @Override
    public void add(Task task) { // Добавление просмотра задачи в список
        if (TASK_HISTORY.containsKey(task.getId())) {
            NODES.remove(TASK_HISTORY.get(task.getId()));
            remove(task.getId());
        }
        TASK_HISTORY.put(task.getId(), linkLast(task));
        NODES.add(TASK_HISTORY.get(task.getId()));
    }

    @Override
    public List<Task> getHistory() { // Получение списка просмотров задач
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (!TASK_HISTORY.isEmpty()) {
            NODES.remove(TASK_HISTORY.get(id));
            removeNode(TASK_HISTORY.get(id));
            TASK_HISTORY.remove(id);
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
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> taskNode : NODES) {
            tasks.add(taskNode.data);
        }
        return tasks;
    }
}