package service;

import manager.HistoryManager;
import manager.Managers;
import model.Epic;
import model.SubTask;
import model.Task;
import service.exception.ManagerSaveException;
import service.enums.Status;
import service.enums.Type;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String FILE;

    public FileBackedTasksManager(String file) {
        this.FILE = "src/resources/" + file;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, StandardCharsets.UTF_8))) {
            String title = " id,type,name,status,description,startTime,duration,endTime,epic\n";
            String history = historyToString(Managers.getDefaultHistory());
            writer.write(title);
            for (Task task : super.getAllTask()) {
                writer.write(toStringTask(task));
            }
            for (Epic epic : super.getAllEpic()) {
                writer.write(toStringEpic(epic));
            }
            for (SubTask subTask : super.getAllSubTask()) {
                writer.write(toStringSubTask(subTask));
            }
            if (!history.isEmpty()) {
                writer.write("\n" + history);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи!");
        }
    }

    @Override
    public void developTask(Task task) {
        super.developTask(task);
        save();
    }

    @Override
    public void developSubTask(SubTask subTask) {
        super.developSubTask(subTask);
        save();
    }

    @Override
    public void developEpic(Epic epic) {
        super.developEpic(epic);
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public void updateTask(int id, Task task, Status status) {
        super.updateTask(id, task, status);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubTask(int id, SubTask subTask, Status status) {
        super.updateSubTask(id, subTask, status);
        save();
    }

    @Override
    public void deleteByIdTask(int id) {
        super.deleteByIdTask(id);
        save();
    }

    @Override
    public void deleteByIdEpic(int id) {
        super.deleteByIdEpic(id);
        save();
    }

    @Override
    public void deleteByIdSubTask(int id) {
        super.deleteByIdSubTask(id);
        save();
    }

    @Override
    public Task getByIdTask(int id) { // Получение обычной задачи по id.
        Task task = super.getByIdTask(id);
        save();
        return task;
    }

    @Override
    public Epic getByIdEpic(int id) { // Получение большой задачи по id.
        Epic epic = super.getByIdEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getByIdSubTask(int id) { // Получение подзадачи для большой задачи по id.
        SubTask subTask = super.getByIdSubTask(id);
        save();
        return subTask;
    }

    private String toStringTask(Task task) {
        return String.format(" %d,%s,%s,%s,%s,%s,%d,%s\n",
                task.getId(), Type.TASK, task.getName(), task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration(), task.getEndTime());
    }

    private String toStringEpic(Epic epic) {
        return String.format(" %d,%s,%s,%s,%s,%s,%d,%s\n",
                epic.getId(), Type.EPIC, epic.getName(), epic.getStatus(), epic.getDescription(), epic.getStartTime(), epic.getDuration(), epic.getEndTimeForEpic());
    }

    private String toStringSubTask(SubTask subTask) {
        return String.format(" %d,%s,%s,%s,%s,%s,%d,%s,%d\n",
                subTask.getId(), Type.SUBTASK, subTask.getName(),
                subTask.getStatus(), subTask.getDescription(), subTask.getStartTime(), subTask.getDuration(), subTask.getEndTime(), subTask.getIdEpic());
    }

    private Task fromStringTask(String value) {
        String[] values = value.split(",");
        Task task = new Task(values[2], values[4]);
        task.setId(Integer.parseInt(values[0]));
        task.setStatus(Status.valueOf(values[3]));
        task.setStartTime(values[5]);
        task.setDuration(Long.parseLong(values[6]));
        return task;
    }

    private Epic fromStringEpic(String value) {
        String[] values = value.split(",");
        Epic epic = new Epic(values[2], values[4]);
        epic.setId(Integer.parseInt(values[0]));
        epic.setStatus(Status.valueOf(values[3]));
        epic.setStartTime(values[5]);
        epic.setDuration(Long.parseLong(values[6]));
        epic.setEndTime(values[7]);
        return epic;
    }

    private SubTask fromStringSubTask(String value) {
        String[] values = value.split(",");
        SubTask subTask = new SubTask(values[2], values[4]);
        subTask.setId(Integer.parseInt(values[0]));
        subTask.setStatus(Status.valueOf(values[3]));
        subTask.setStartTime(values[5]);
        subTask.setDuration(Long.parseLong(values[6]));
        subTask.setIdEpic(Integer.parseInt(values[8]));
        return subTask;
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        if (sb.length() != 0) {
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return "";
        }
    }

    private static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String str : values) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.read() != -1) {
                String line = reader.readLine();
                String[] str = line.split(",");
                if (!line.startsWith("id")) {
                    if (str[1].equals(Type.TASK.toString())) {
                        Task task = fileBackedTasksManager.fromStringTask(line);
                        fileBackedTasksManager.TASKS.put(Integer.parseInt(str[0]), task);
                    } else if (str[1].equals(Type.EPIC.toString())) {
                        Epic epic = fileBackedTasksManager.fromStringEpic(line);
                        fileBackedTasksManager.EPICS.put(Integer.parseInt(str[0]), epic);
                    } else if (str[1].equals(Type.SUBTASK.toString())) {
                        SubTask subTask = fileBackedTasksManager.fromStringSubTask(line);
                        fileBackedTasksManager.EPICS.get(subTask.getIdEpic()).putSubTask(subTask.getId(), subTask);
                        fileBackedTasksManager.SUB_TASKS.put(Integer.parseInt(str[0]), subTask);
                    } else {
                        for (Integer id : FileBackedTasksManager.historyFromString(line)) {
                            if (fileBackedTasksManager.TASKS.containsKey(id)) {
                                Managers.getDefaultHistory().add(fileBackedTasksManager.TASKS.get(id));
                            } else if (fileBackedTasksManager.EPICS.containsKey(id)) {
                                Managers.getDefaultHistory().add(fileBackedTasksManager.EPICS.get(id));
                            } else {
                                Managers.getDefaultHistory().add(fileBackedTasksManager.SUB_TASKS.get(id));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения!");
        }
        return fileBackedTasksManager;
    }
}
