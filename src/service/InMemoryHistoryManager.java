package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static List<Task> TASK_HISTORY = new ArrayList<>(); // Список просмотра задач

    @Override
    public void add(Task task) { // Добавление просмотра задачи в список
        if (TASK_HISTORY.size() == 10) {
            TASK_HISTORY.remove(0);
        }
        TASK_HISTORY.add(task);
    }

    @Override
    public List<Task> getHistory() { // Получение списка просмотров задач
        return TASK_HISTORY;
    }
}