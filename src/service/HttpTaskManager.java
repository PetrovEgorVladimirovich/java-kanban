package service;

import KVServer.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import model.Epic;
import model.SubTask;
import model.Task;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;
    public static final String TASKS_KEY = "tasks";
    public static final String SUBTASKS_KEY = "subtasks";
    public static final String EPICS_KEY = "epics";
    public static final String HISTORY_KEY = "history";

    public HttpTaskManager(String host, int port) {
        super(null);
        this.client = new KVTaskClient(host, port);
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(getAllTask());
        client.put(TASKS_KEY, jsonTasks);
        String jsonSubTasks = gson.toJson(getAllSubTask());
        client.put(SUBTASKS_KEY, jsonSubTasks);
        String jsonEpics = gson.toJson(getAllEpic());
        client.put(EPICS_KEY, jsonEpics);
        List<Integer> historyIds = getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String jsonHistory = gson.toJson(historyIds);
        client.put(HISTORY_KEY, jsonHistory);
    }

    public void load() {
        String jsonTasks = client.load(TASKS_KEY);
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(jsonTasks, taskType);
        for (Task task : tasks) {
            TASKS.put(task.getId(), task);
        }

        String jsonSubTasks = client.load(SUBTASKS_KEY);
        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subTasks = gson.fromJson(jsonSubTasks, subTaskType);
        for (SubTask subTask : subTasks) {
            SUB_TASKS.put(subTask.getId(), subTask);
        }

        String jsonEpics = client.load(EPICS_KEY);
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, epicType);
        for (Epic epic : epics) {
            EPICS.put(epic.getId(), epic);
        }

        String jsonHistoryIds = client.load(HISTORY_KEY);
        Type historyIdsType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> historyIds = gson.fromJson(jsonHistoryIds, historyIdsType);
        for (Integer id : historyIds) {
            Managers.getDefaultHistory().add(findTask(id));
        }
    }

    protected Task findTask(Integer id) {
        final Task task = TASKS.get(id);
        if (Objects.nonNull(task)) {
            return task;
        }

        final SubTask subTask = SUB_TASKS.get(id);
        if (Objects.nonNull(subTask)) {
            return subTask;
        }
        return EPICS.get(id);
    }
}
