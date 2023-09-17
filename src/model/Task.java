package model;

import service.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected LocalDateTime startTime = null;
    protected long duration = 0;
    protected LocalDateTime endTime = null;
    protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        if (startTime != null) {
            return startTime.format(formatter);
        }
        return null;
    }

    public long getDuration() {
        return duration;
    }

    public void setStartTime(String startTime) {
        if (!startTime.equals("null")) {
            this.startTime = LocalDateTime.parse(startTime, formatter);
        } else {
            this.startTime = null;
        }
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getEndTime() {
        if (startTime != null) {
            endTime = startTime.plus(Duration.ofMinutes(duration));
            return endTime.format(formatter);
        }
        return null;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + duration + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                '}';
    }

}
