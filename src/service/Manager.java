package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>(); // Хранение обычных задач.
    private final HashMap<Integer, Epic> epics = new HashMap<>(); // Хранение больших задач.
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Хранение подзадач для больших задач.

    private int id = 0;
    private int addId() {
        return ++id;
    } // Генерация id.
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    } // Получение списка всех обычных задач.
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    } // Получение списка всех больших задач.
     public List<SubTask> getAllSubTask() { // Получение списка всех подзадач для больших задач.
        return new ArrayList<>(subTasks.values());
    }
    public void clearTask() {
        tasks.clear();
    } // Удаление всех обычных задач.
    public void clearEpic() { // Удаление всех больших задач.
        epics.clear();
        subTasks.clear();
    }
    public void clearSubTask() { // Удаление всех подзадач для больших задач.
        subTasks.clear();
        for(Epic epicTest : epics.values()) {
            epicTest.clearHashMapSubTask();
            countStatusEpic(epicTest);
        }
    }
    public Task getByIdTask(int id) { // Получение обычной задачи по id.
        return tasks.get(id);
    }
    public Epic getByIdEpic(int id) { // Получение большой задачи по id.
        return epics.get(id);
    }
    public SubTask getByIdSubTask(int id) { // Получение подзадачи для большой задачи по id.
        return subTasks.get(id);
    }
    public Task developTask(Task task) { // Создание обычной задачи.
        task.setId(addId());
        task.setStatus("New");
        tasks.put(task.getId(), task);
        return task;
    }
    public SubTask developSubTask(SubTask subTask) { // Создание подзадачи для большой задачи.
        subTask.setId(addId());
        subTask.setStatus("New");
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }
    public Epic developEpic(Epic epic) { // Создание большой задачи.
        epic.setId(addId());
        epic.setStatus("New");
        epics.put(epic.getId(), epic);
        return epic;
    }
    public void updateTask(int id, Task task, String status) { // Обновление обычной задачи.
        if(tasks.containsKey(id)) {
            task.setStatus(status);
            tasks.put(id, task);
        }
    }
    public void updateEpic(int id, Epic epic){ // Обновление большой задачи.
        if(epics.containsKey(id)) {
            Epic epicLast = epics.get(id);
            HashMap<Integer, SubTask> subTask = epicLast.getHashMapSubTask();
            epic.setSubTask(subTask);
            epics.put(id, epic);
        }
    }
    public void updateSubTask(int id, SubTask subTask, String status){ // Обновление подзадачи для большой задачи.
        if(subTasks.containsKey(id)) {
            subTask.setStatus(status);
            SubTask subTaskLast = subTasks.get(id);
            Epic epic = epics.get(subTaskLast.getIdEpic());
            subTask.setIdEpic(subTaskLast.getIdEpic());
            subTasks.put(id, subTask);
            epic.putSubTask(subTask.getId(), subTask);
            countStatusEpic(epic);
        }
    }
    public void deleteByIdTask(int id) { // Удаление обычной задачи по id.
        tasks.remove(id);
    }
    public void deleteByIdEpic(int id) { // Удаление большой задачи по id.
        Epic epic = epics.get(id);
        for (Integer key : epic.getHashMapSubTask().keySet()){
            subTasks.remove(key);
        }
        epics.remove(id);
    }
    public void deleteByIdSubTask(int id) { // Удаление подзадачи для большой задачи по id.
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getIdEpic());
        epic.removeSubTask(id);
        subTasks.remove(id);
        countStatusEpic(epic);
    }
    public List<SubTask> getSubTaskByEpic(Epic epic) { // Получение списка подзадач у конкретной большой задачи.
        if(epics.containsValue(epic)) {
            return epic.getSubTask();
        } else {
            return null;
        }
    }
    public void countStatusEpic(Epic epic){ // Анализ статуса большой задачи.
        epic.setStatus("New");
        boolean isSubTaskNew = false;
        boolean isSubTaskDone = false;
        boolean isSubTaskInProgress = false;
        for(SubTask subTask : epic.getSubTask()) {
            if(subTask.getStatus().equals("New")){
                isSubTaskNew = true;
            } else if (subTask.getStatus().equals("Done")) {
                isSubTaskDone = true;
            } else {
                isSubTaskInProgress = true;
            }
            }
       if (isSubTaskNew) {
           epic.setStatus("New");
        }
       if (isSubTaskDone) {
           epic.setStatus("Done");
       }
       if (isSubTaskDone && isSubTaskNew) {
           epic.setStatus("In_Progress");
       }
       if (isSubTaskInProgress) {
           epic.setStatus("In_Progress");
       }
    }
    }
