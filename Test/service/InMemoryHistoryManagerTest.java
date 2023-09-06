package service;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        task = new Task("Обычная задача.", "Описание: обычная задача.");
        task.setStartTime("22.12.2022 00:30");
        task.setDuration(90);
        taskManager.developTask(task);
        task1 = new Task("Обычная задача 1.", "Описание: обычная задача 1.");
        task1.setStartTime("22.12.2022 03:30");
        task1.setDuration(90);
        taskManager.developTask(task1);
        task2 = new Task("Обычная задача 2.", "Описание: обычная задача 2.");
        task2.setStartTime("22.12.2022 06:30");
        task2.setDuration(90);
        taskManager.developTask(task2);
    }

    private void addTasks() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
    }

    @Test
    void shouldBeEmptyTaskHistory() {
        historyManager.clear();
        List<Task> taskList = historyManager.getHistory();
        assertEquals(0, taskList.size(), "История должна быть пустая!");
    }

    @Test
    void duplicateShouldBeDeleted() {
        addTasks();
        historyManager.add(task);
        List<Task> taskList = historyManager.getHistory();
        assertEquals(3, taskList.size(), "Дубликат задачи должен быть перезаписан!");
    }

    @Test
    void shouldBeDeletionOfFirstElement() {
        addTasks();
        historyManager.remove(historyManager.getHistory().get(0).getId());
        List<Task> taskList = historyManager.getHistory();
        assertEquals(task1, taskList.get(0), "Должен быть удалён первый элемент!");
    }

    @Test
    void shouldBeDeletionOfMiddleElement() {
        addTasks();
        historyManager.remove(historyManager.getHistory().get(1).getId());
        List<Task> taskList = historyManager.getHistory();
        assertFalse(taskList.contains(task1), "Должен быть удалён средний элемент!");
    }

    @Test
    void shouldBeDeletionOfLastElement() {
        addTasks();
        historyManager.remove(historyManager.getHistory().get(2).getId());
        List<Task> taskList = historyManager.getHistory();
        assertFalse(taskList.contains(task2), "Должен быть удалён последний элемент!");
    }
}