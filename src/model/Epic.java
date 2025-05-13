package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        if (duration == null) {
            setDuration(Duration.ZERO);
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public List<Integer> getSubTasksIds() {
        return subTasks;
    }

    public void addSubTaskId(int id) {
        if (id == getId()) {
            throw new IllegalArgumentException("Subtask ID cannot be equal to Epic ID");
        }
        subTasks.add(id);
    }

    public void recalculateFields() {
        setDuration(Duration.ZERO);
        setStartTime(null);
        endTime = null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTasks +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}