package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;

    @Override
    public int addId() {
        return ++id;
    } // Генерация id.

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(TASKS.values());
    } // Получение списка всех обычных задач.

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(EPICS.values());
    } // Получение списка всех больших задач.

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(SUB_TASKS.values());
    } // Получение списка всех подзадач для больших задач.

    @Override
    public void clearTask() {
        TASKS.clear();
    } // Удаление всех обычных задач.

    @Override
    public void clearEpic() { // Удаление всех больших задач.
        EPICS.clear();
        SUB_TASKS.clear();
    }

    @Override
    public void clearSubTask() { // Удаление всех подзадач для больших задач.
        SUB_TASKS.clear();
        for (Epic epicTest : EPICS.values()) {
            epicTest.clearHashMapSubTask();
            countStatusEpic(epicTest);
        }
    }

    @Override
    public Task getByIdTask(int id) { // Получение обычной задачи по id.
        Managers.getDefaultHistory().add(TASKS.get(id));
        return TASKS.get(id);
    }

    @Override
    public Epic getByIdEpic(int id) { // Получение большой задачи по id.
        Managers.getDefaultHistory().add(EPICS.get(id));
        return EPICS.get(id);
    }

    @Override
    public SubTask getByIdSubTask(int id) { // Получение подзадачи для большой задачи по id.
        Managers.getDefaultHistory().add(SUB_TASKS.get(id));
        return SUB_TASKS.get(id);
    }

    @Override
    public Task developTask(Task task) { // Создание обычной задачи.
        task.setId(addId());
        task.setStatus(Status.NEW);
        TASKS.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask developSubTask(SubTask subTask) { // Создание подзадачи для большой задачи.
        subTask.setId(addId());
        subTask.setStatus(Status.NEW);
        SUB_TASKS.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Epic developEpic(Epic epic) { // Создание большой задачи.
        epic.setId(addId());
        epic.setStatus(Status.NEW);
        EPICS.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateTask(int id, Task task, Status status) { // Обновление обычной задачи.
        if (TASKS.containsKey(id)) {
            task.setStatus(status);
            TASKS.put(id, task);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) { // Обновление большой задачи.
        if (EPICS.containsKey(id)) {
            Epic epicLast = EPICS.get(id);
            HashMap<Integer, SubTask> subTask = epicLast.getHashMapSubTask();
            epic.setSubTask(subTask);
            EPICS.put(id, epic);
        }
    }

    @Override
    public void updateSubTask(int id, SubTask subTask, Status status) { // Обновление подзадачи для большой задачи.
        if (SUB_TASKS.containsKey(id)) {
            subTask.setStatus(status);
            SubTask subTaskLast = SUB_TASKS.get(id);
            Epic epic = EPICS.get(subTaskLast.getIdEpic());
            subTask.setIdEpic(subTaskLast.getIdEpic());
            SUB_TASKS.put(id, subTask);
            epic.putSubTask(subTask.getId(), subTask);
            countStatusEpic(epic);
        }
    }

    @Override
    public void deleteByIdTask(int id) { // Удаление обычной задачи по id.
        TASKS.remove(id);
    }

    @Override
    public void deleteByIdEpic(int id) { // Удаление большой задачи по id.
        Epic epic = EPICS.get(id);
        for (Integer key : epic.getHashMapSubTask().keySet()) {
            SUB_TASKS.remove(key);
        }
        EPICS.remove(id);
    }

    @Override
    public void deleteByIdSubTask(int id) { // Удаление подзадачи для большой задачи по id.
        SubTask subTask = SUB_TASKS.get(id);
        Epic epic = EPICS.get(subTask.getIdEpic());
        epic.removeSubTask(id);
        SUB_TASKS.remove(id);
        countStatusEpic(epic);
    }

    @Override
    public List<SubTask> getSubTaskByEpic(Epic epic) { // Получение списка подзадач у конкретной большой задачи.
        if (EPICS.containsValue(epic)) {
            return epic.getSubTask();
        } else {
            return null;
        }
    }

    @Override
    public void countStatusEpic(Epic epic) { // Анализ статуса большой задачи.
        epic.setStatus(Status.NEW);
        boolean isSubTaskNew = false;
        boolean isSubTaskDone = false;
        boolean isSubTaskInProgress = false;
        for (SubTask subTask : epic.getSubTask()) {
            if (subTask.getStatus().equals(Status.NEW)) {
                isSubTaskNew = true;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                isSubTaskDone = true;
            } else {
                isSubTaskInProgress = true;
            }
        }
        if (isSubTaskNew) {
            epic.setStatus(Status.NEW);
        }
        if (isSubTaskDone) {
            epic.setStatus(Status.DONE);
        }
        if (isSubTaskDone && isSubTaskNew) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (isSubTaskInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}