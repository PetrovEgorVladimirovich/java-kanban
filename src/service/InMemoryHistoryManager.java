package service;

import manager.HistoryManager;
import model.Task;
import service.assistants.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static Node<Task> last;

    private static final Map<Integer, Node<Task>> TASK_HISTORY = new HashMap<>();

    private static final List<Node<Task>> NODES = new ArrayList<>();

    @Override
    public void add(Task task) { // Добавление просмотра задачи в список
        TASK_HISTORY.computeIfPresent(task.getId(), (k, v) -> {
            NODES.remove(TASK_HISTORY.get(k));
            remove(k);
            return null;
        });
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
        }
    }

    private Node<Task> linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);

        last = newNode;
        if (l != null) {
            l.next = newNode;
        }
        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            if (prev != null) {
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
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> taskNode : NODES) {
            tasks.add(taskNode.data);
        }
        return tasks;
    }

    @Override
    public void clear() {
        TASK_HISTORY.clear();
        NODES.clear();
    }
}