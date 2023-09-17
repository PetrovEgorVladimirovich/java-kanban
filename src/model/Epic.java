package model;

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

    public void setEndTime(String endTime) {
        if (!endTime.equals("null")) {
            this.endTime = LocalDateTime.parse(endTime, formatter);
        } else {
            this.endTime = null;
        }
    }

    public String getEndTimeForEpic() {
        if (endTime != null) {
            return endTime.format(formatter);
        }
        return null;
    }

    public void countTime() {
        long AllDuration = 0;
        LocalDateTime dateTimeStart = null;
        LocalDateTime dataTimeEnd = null;
        for (SubTask subTask : getSubTasks()) {
            AllDuration += subTask.getDuration();
            if (subTask.startTime != null) {
                if (dateTimeStart == null || dateTimeStart.isAfter(subTask.startTime)) {
                    dateTimeStart = subTask.startTime;
                }
                if (dataTimeEnd == null || dataTimeEnd.isBefore(subTask.endTime)) {
                    dataTimeEnd = subTask.endTime;
                }
            }
        }
        this.startTime = dateTimeStart;
        this.duration = AllDuration;
        this.endTime = dataTimeEnd;
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
                ", duration=" + duration + '\'' +
                ", endTime=" + getEndTimeForEpic() + '\'' +
                '}';
    }
}
