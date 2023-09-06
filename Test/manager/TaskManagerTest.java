package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.assistants.Status;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    protected void create() {
        task = new Task("Обычная задача.", "Описание: обычная задача.");
        task.setStartTime("22.12.2022 00:30");
        task.setDuration(90);
        taskManager.developTask(task);
        epic = new Epic("Большая задача.", "Описание: большая задача.");
        subTask = new SubTask("Подзадача.", "Описание: подзадача.");
        subTask.setStartTime("22.12.2022 03:30");
        subTask.setDuration(90);
        taskManager.developSubTask(subTask);
        connect();
    }

    private void connect() { // Метод для связи эпиков и сабтасок.
        epic.putSubTask(subTask.getId(), subTask);
        epic.countTime();
        taskManager.developEpic(epic);
        subTask.setIdEpic(epic.getId());
    }

    @Test
    void shouldGetAllTask() {
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(1, taskList.size(), "Неверное кол-во обычных задач!");
    }

    @Test
    void shouldGetAllEpic() {
        List<Epic> epicList = taskManager.getAllEpic();
        assertEquals(1, epicList.size(), "Неверное кол-во больших задач!");
    }

    @Test
    void shouldGetAllSubTask() {
        List<SubTask> subTaskList = taskManager.getAllSubTask();
        assertEquals(1, subTaskList.size(), "Неверное кол-во подзадач!");
    }

    @Test
    void shouldClearTask() {
        taskManager.clearTask();
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(0, taskList.size(), "Обычных задач быть не должно!");
    }

    @Test
    void shouldClearEpic() {
        taskManager.clearEpic();
        List<Epic> epicList = taskManager.getAllEpic();
        assertEquals(0, epicList.size(), "Больших задач быть не должно!");
    }

    @Test
    void shouldClearSubTask() {
        taskManager.clearSubTask();
        List<SubTask> subTaskList = taskManager.getAllSubTask();
        assertEquals(0, subTaskList.size(), "Подзадач быть не должно!");
    }

    @Test
    void shouldGetByIdTask() {
        Task task1 = taskManager.getByIdTask(task.getId());
        assertEquals(task, task1, "Обычные задачи не совпадают!");
        task1 = taskManager.getByIdTask(task.getId() + 1);
        assertNull(task1, "Обычная задача должна быть null!");
    }

    @Test
    void shouldGetByIdEpic() {
        Epic epic1 = taskManager.getByIdEpic(epic.getId());
        assertEquals(epic, epic1, "Большие задачи не совпадают!");
        epic1 = taskManager.getByIdEpic(epic.getId() + 1);
        assertNull(epic1, "Большая задача должна быть null!");
    }

    @Test
    void shouldGetByIdSubTask() {
        SubTask subTask1 = taskManager.getByIdSubTask(subTask.getId());
        assertEquals(subTask, subTask1, "Подзадачи не совпадают!");
        subTask1 = taskManager.getByIdSubTask(subTask.getId() + 1);
        assertNull(subTask1, "Подзадача должна быть null!");
    }

    @Test
    void shouldDevelopTask() {
        Task task1 = new Task("Обычная задача.", "Описание: обычная задача.");
        task1.setStartTime("22.12.2022 16:30");
        task1.setDuration(90);
        taskManager.developTask(task1);
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(2, taskList.size(), "Обычная задача создаётся некорректно!");
    }

    @Test
    void shouldDevelopSubTask() {
        SubTask subTask1 = new SubTask("Подзадача.", "Описание: подзадача.");
        subTask1.setStartTime("22.12.2022 16:30");
        subTask1.setDuration(90);
        taskManager.developSubTask(subTask1);
        List<SubTask> subTaskList = taskManager.getAllSubTask();
        assertEquals(2, subTaskList.size(), "Подзадача создаётся некорректно!");
    }

    @Test
    void shouldDevelopEpic() {
        taskManager.developEpic(epic);
        List<Epic> epicList = taskManager.getAllEpic();
        assertEquals(2, epicList.size(), "Большая задача создаётся некорректно!");
    }

    @Test
    void shouldUpdateTask() {
        taskManager.updateTask(task.getId(), task, Status.DONE);
        assertEquals(Status.DONE, task.getStatus(), "Статус обновлён некорректно!");
        taskManager.updateTask(task.getId() + 1, task, Status.NEW);
        assertEquals(Status.DONE, task.getStatus(), "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearTask();
        taskManager.updateTask(task.getId(), task, Status.IN_PROGRESS);
        assertEquals(0, taskManager.getAllTask().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic1 = new Epic("Большая задача 1.", "Описание: большая задача 1.");
        epic1.setStartTime(epic.getStartTime());
        epic1.setDuration(epic.getDuration());
        taskManager.updateEpic(epic.getId(), epic1);
        assertEquals(epic1, taskManager.getAllEpic().get(0), "Обновление работает некорректно!");
        taskManager.updateEpic(epic.getId() + 1, epic);
        assertFalse(taskManager.getAllEpic().contains(epic), "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearEpic();
        taskManager.updateEpic(epic1.getId(), epic);
        assertEquals(0, taskManager.getAllEpic().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldUpdateSubTask() {
        taskManager.updateSubTask(subTask.getId(), subTask, Status.DONE);
        assertEquals(Status.DONE, subTask.getStatus(), "Статус обновлён некорректно!");
        taskManager.updateSubTask(subTask.getId() + 1, subTask, Status.NEW);
        assertEquals(Status.DONE, subTask.getStatus(), "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearSubTask();
        taskManager.updateTask(subTask.getId(), subTask, Status.IN_PROGRESS);
        assertEquals(0, taskManager.getAllSubTask().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldDeleteByIdTask() {
        taskManager.deleteByIdTask(task.getId());
        assertEquals(0, taskManager.getAllTask().size(), "Удаление задачи проходит некорректно!");
        taskManager.developTask(task);
        taskManager.deleteByIdTask(task.getId() + 1);
        assertEquals(1, taskManager.getAllTask().size(),
                "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearTask();
        taskManager.deleteByIdTask(task.getId());
        assertEquals(0, taskManager.getAllTask().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldDeleteByIdEpic() {
        taskManager.deleteByIdEpic(epic.getId());
        assertEquals(0, taskManager.getAllEpic().size(),
                "Удаление большой задачи проходит некорректно!");
        taskManager.developEpic(epic);
        taskManager.deleteByIdEpic(epic.getId() + 1);
        assertEquals(1, taskManager.getAllEpic().size(),
                "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearEpic();
        taskManager.deleteByIdEpic(epic.getId());
        assertEquals(0, taskManager.getAllEpic().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldDeleteByIdSubTask() {
        taskManager.deleteByIdSubTask(subTask.getId());
        assertEquals(0, taskManager.getAllSubTask().size(), "Удаление подзадачи проходит некорректно!");
        taskManager.developSubTask(subTask);
        taskManager.deleteByIdSubTask(subTask.getId() + 1);
        assertEquals(1, taskManager.getAllSubTask().size(),
                "Некорректно обрабатывается случай с неверным id!");
        taskManager.clearSubTask();
        taskManager.deleteByIdSubTask(subTask.getId());
        assertEquals(0, taskManager.getAllSubTask().size(), "Хранилище должно быть пустым!");
    }

    @Test
    void shouldGetSubTaskByEpic() {
        List<SubTask> subTaskList = taskManager.getSubTaskByEpic(epic);
        assertEquals(subTask, subTaskList.get(0), "Подзадача полученная по большой задаче, некорректна!");
        taskManager.clearSubTask();
        taskManager.clearEpic();
        assertNull(taskManager.getSubTaskByEpic(epic), "Некорректная обработка с пустым списком!");
    }

    @Test
    void shouldCountStatusEpic() {
        subTask.setStatus(Status.DONE);
        taskManager.countStatusEpic(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Статус у большой задачи должен быть DONE!");
        subTask.setStatus(Status.IN_PROGRESS);
        taskManager.countStatusEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус у большой задачи должен быть IN_PROGRESS!");
    }

    @Test
    void shouldGetHistory() {
        List<Task> historyList = taskManager.getHistory();
        assertNotNull(historyList, "История должна быть не пустая!");
    }

    @Test
    void shouldGetPrioritizedTasks() {
        Set<Task> tasks = taskManager.getPrioritizedTasks();
        assertEquals(2, tasks.size(), "Неверное кол-во задач!");
    }
}