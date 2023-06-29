package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); // Добавление просмотра задачи в список

    List<Task> getHistory();

    void remove(int id);
}