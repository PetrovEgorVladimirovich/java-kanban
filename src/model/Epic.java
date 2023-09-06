package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void clearHashMapSubTask() {
        subTasks.clear();
    }

    public HashMap<Integer, SubTask> getHashMapSubTask() {
        return subTasks;
    }

    public void setSubTasks(HashMap<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void putSubTask(int id, SubTask subTask) {
        this.subTasks.put(id, subTask);
    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
    }

    public void countTime() {
        long AllDuration = 0;
        for (SubTask subTask : getSubTasks()) {
            AllDuration += subTask.getDuration();
            if (this.startTime == null || this.startTime.isAfter(subTask.startTime)) {
                this.startTime = LocalDateTime.parse(subTask.getStartTime(), formatter);
            }
        }
        this.duration = Duration.ofMinutes(AllDuration);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTask=" + subTasks.keySet() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + getDuration() + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                '}';
    }
}
