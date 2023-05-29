package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {

    private HashMap<Integer, SubTask> subTask = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<SubTask> getSubTask() {
        return new ArrayList<>(subTask.values());
    }

    public void clearHashMapSubTask() {
        subTask.clear();
    }

    public HashMap<Integer, SubTask> getHashMapSubTask() {
        return subTask;
    }

    public void setSubTask(HashMap<Integer, SubTask> subTask) {
        this.subTask = subTask;
    }

    public void putSubTask(int id, SubTask subTask) {
        this.subTask.put(id, subTask);
    }

    public void removeSubTask(int id) {
        subTask.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTask=" + subTask.keySet() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
