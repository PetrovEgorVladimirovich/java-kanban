package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import service.assistants.Status;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();// Получение списка всех обычных задач.

    List<Epic> getAllEpic();// Получение списка всех больших задач.

    List<SubTask> getAllSubTask();// Получение списка всех подзадач для больших задач.

    void clearTask();// Удаление всех обычных задач.

    void clearEpic();// Удаление всех больших задач.

    void clearSubTask();// Удаление всех подзадач для больших задач.


    Task getByIdTask(int id);// Получение обычной задачи по id.

    Epic getByIdEpic(int id);// Получение большой задачи по id.


    SubTask getByIdSubTask(int id);// Получение подзадачи для большой задачи по id.

    Task developTask(Task task);// Создание обычной задачи.

    SubTask developSubTask(SubTask subTask);// Создание подзадачи для большой задачи.

    Epic developEpic(Epic epic);// Создание большой задачи.

    void updateTask(int id, Task task, Status status);// Обновление обычной задачи.

    void updateEpic(int id, Epic epic);// Обновление большой задачи.

    void updateSubTask(int id, SubTask subTask, Status status);// Обновление подзадачи для большой задачи.

    void deleteByIdTask(int id);// Удаление обычной задачи по id.

    void deleteByIdEpic(int id);// Удаление большой задачи по id.

    void deleteByIdSubTask(int id);// Удаление подзадачи для большой задачи по id.

    List<SubTask> getSubTaskByEpic(Epic epic);// Получение списка подзадач у конкретной большой задачи.

    void countStatusEpic(Epic epic);// Анализ статуса большой задачи.

    List<Task> getHistory(); // Получение списка просмотров задач
}