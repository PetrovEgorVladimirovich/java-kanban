package service;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import service.assistants.Status;
import service.assistants.TaskValidationException;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> TASKS = new HashMap<>(); // Хранение обычных задач.
    protected HashMap<Integer, Epic> EPICS = new HashMap<>(); // Хранение больших задач.
    protected HashMap<Integer, SubTask> SUB_TASKS = new HashMap<>(); // Хранение подзадач для больших задач.
    private int id = 0;

    private final Comparator<Task> comparator = (o1, o2) -> {
        if (LocalDateTime.parse(o1.getStartTime(), o1.getFormatter())
                .isAfter(LocalDateTime.parse(o2.getStartTime(), o2.getFormatter()))) {
            return 1;
        } else if (LocalDateTime.parse(o1.getStartTime(), o2.getFormatter())
                .isBefore(LocalDateTime.parse(o2.getStartTime(), o2.getFormatter()))) {
            return -1;
        } else {
            return 0;
        }
    };

    private final Set<Task> tasksSorted = new TreeSet<>(comparator);

    private int addId() {
        return ++id;
    } // Генерация id.

    private void validate(Task task) {
        LocalDateTime startTime = LocalDateTime.parse(task.getStartTime(), task.getFormatter());
        LocalDateTime endTime = LocalDateTime.parse(task.getEndTime(), task.getFormatter());
        int result = 0;

        for (Task taskSort : tasksSorted) {
            LocalDateTime startTimeTask = LocalDateTime.parse(taskSort.getStartTime(), taskSort.getFormatter());
            LocalDateTime endTimeTask = LocalDateTime.parse(taskSort.getEndTime(), taskSort.getFormatter());
            if (startTime.isAfter(startTimeTask) && startTime.isBefore(endTimeTask)) {
                result = 1;
            }
            if (endTime.isAfter(startTimeTask) && endTime.isBefore(endTimeTask)) {
                result = 1;
            }
            if (startTime.equals(startTimeTask)) {
                result = 1;
            }
            if (endTime.equals(endTimeTask)) {
                result = 1;
            }
        }
        if (result == 1) {
            throw new TaskValidationException("Задача пересекается!");
        }
    }

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
        tasksSorted.removeIf(task -> TASKS.containsValue(task));
        TASKS.clear();
    } // Удаление всех обычных задач.

    @Override
    public void clearEpic() { // Удаление всех больших задач.
        EPICS.clear();
        tasksSorted.removeIf(subtask -> SUB_TASKS.containsValue(subtask));
        SUB_TASKS.clear();
    }

    @Override
    public void clearSubTask() { // Удаление всех подзадач для больших задач.
        tasksSorted.removeIf(subtask -> SUB_TASKS.containsValue(subtask));
        SUB_TASKS.clear();
        for (Epic epicTest : EPICS.values()) {
            epicTest.clearHashMapSubTask();
            countStatusEpic(epicTest);
        }
    }

    @Override
    public Task getByIdTask(int id) { // Получение обычной задачи по id.
        if (TASKS.containsKey(id)) {
            Managers.getDefaultHistory().add(TASKS.get(id));
            return TASKS.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getByIdEpic(int id) { // Получение большой задачи по id.
        if (EPICS.containsKey(id)) {
            Managers.getDefaultHistory().add(EPICS.get(id));
            return EPICS.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getByIdSubTask(int id) { // Получение подзадачи для большой задачи по id.
        if (SUB_TASKS.containsKey(id)) {
            Managers.getDefaultHistory().add(SUB_TASKS.get(id));
            return SUB_TASKS.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Task developTask(Task task) { // Создание обычной задачи.
        validate(task);
        task.setId(addId());
        task.setStatus(Status.NEW);
        TASKS.put(task.getId(), task);
        tasksSorted.add(task);
        return task;
    }

    @Override
    public SubTask developSubTask(SubTask subTask) { // Создание подзадачи для большой задачи.
        validate(subTask);
        subTask.setId(addId());
        subTask.setStatus(Status.NEW);
        SUB_TASKS.put(subTask.getId(), subTask);
        tasksSorted.add(subTask);
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
            tasksSorted.remove(TASKS.get(id));
            task.setStatus(status);
            TASKS.put(id, task);
            tasksSorted.add(task);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) { // Обновление большой задачи.
        if (EPICS.containsKey(id)) {
            Epic epicLast = EPICS.get(id);
            HashMap<Integer, SubTask> subTask = epicLast.getHashMapSubTask();
            epic.setSubTasks(subTask);
            EPICS.put(id, epic);
        }
    }

    @Override
    public void updateSubTask(int id, SubTask subTask, Status status) { // Обновление подзадачи для большой задачи.
        if (SUB_TASKS.containsKey(id)) {
            tasksSorted.remove(SUB_TASKS.get(id));
            subTask.setStatus(status);
            SubTask subTaskLast = SUB_TASKS.get(id);
            Epic epic = EPICS.get(subTaskLast.getIdEpic());
            subTask.setIdEpic(subTaskLast.getIdEpic());
            SUB_TASKS.put(id, subTask);
            epic.putSubTask(subTask.getId(), subTask);
            countStatusEpic(epic);
            tasksSorted.add(subTask);
        }
    }

    @Override
    public void deleteByIdTask(int id) { // Удаление обычной задачи по id.
        if (TASKS.containsKey(id)) {
            tasksSorted.remove(TASKS.get(id));
            Managers.getDefaultHistory().remove(id);
            TASKS.remove(id);
        }
    }

    @Override
    public void deleteByIdEpic(int id) { // Удаление большой задачи по id.
        if (EPICS.containsKey(id)) {
            Managers.getDefaultHistory().remove(id);
            Epic epic = EPICS.get(id);
            for (Integer key : epic.getHashMapSubTask().keySet()) {
                tasksSorted.remove(SUB_TASKS.get(key));
                SUB_TASKS.remove(key);
                Managers.getDefaultHistory().remove(key);
            }
            EPICS.remove(id);
        }
    }

    @Override
    public void deleteByIdSubTask(int id) { // Удаление подзадачи для большой задачи по id.
        if (SUB_TASKS.containsKey(id)) {
            tasksSorted.remove(SUB_TASKS.get(id));
            Managers.getDefaultHistory().remove(id);
            SubTask subTask = SUB_TASKS.get(id);
            Epic epic = EPICS.get(subTask.getIdEpic());
            epic.removeSubTask(id);
            SUB_TASKS.remove(id);
            countStatusEpic(epic);
        }
    }

    @Override
    public List<SubTask> getSubTaskByEpic(Epic epic) { // Получение списка подзадач у конкретной большой задачи.
        if (EPICS.containsValue(epic)) {
            return epic.getSubTasks();
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
        for (SubTask subTask : epic.getSubTasks()) {
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

    @Override
    public List<Task> getHistory() { // Получение списка просмотров задач
        return Managers.getDefaultHistory().getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return tasksSorted;
    }
}