package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List<Task> TASK_HISTORY = new ArrayList<>(); // Список просмотра задач

    void add(Task task); // Добавление просмотра задачи в список

    List<Task> getHistory(); // Получение списка просмотров задач
}
