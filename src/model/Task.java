package model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.time.Duration;

  public class Task {

    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration != null ? duration : Duration.ZERO;
        this.startTime = startTime;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

      public void setDuration(Duration duration) {
          this.duration = duration;
      }

      public void setStartTime(LocalDateTime startTime) {
          this.startTime = startTime;
      }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, id, status);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description=" + description +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", duration=" + getDuration() +
                ", startTime= " + getStartTime() +
                '}';
    }
}
