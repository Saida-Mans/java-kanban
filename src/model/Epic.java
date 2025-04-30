package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, status, null, null);
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = null;
    }

    public List<Integer> getSubTasksIds() {
        return subTasks;
    }

    public  TaskType getType() {
        return TaskType.EPIC;
    }

    public void setSubTasksIds(int id) {
        if (id == super.getId())
            throw new RuntimeException("Subtasks Id equals to Epics Id");
        subTasks.add(id);
    }

    public void recalculateFields() {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subTasks + "name='" + getName() + '\'' +
                ", description=" + getDescription() +
                ", id='" + getId() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration()+
                ", startTime= "+ getStartTime()+
                ", endTime= "+ getEndTime()+
                '}';
    }
}

