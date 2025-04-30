package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() + "name='" + getName() + '\'' +
                ", description=" + getDescription() +
                ", id='" + getId() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime= " + getStartTime() +
                '}';
    }
}