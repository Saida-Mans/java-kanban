  package model;

import TaskType.TaskType;

import java.util.Objects;

public class Task {

    private  String name;
    private  String description;
    private int id;
    private Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;


    }
    public  TaskType getType() {
        return TaskType.TASK;
    }

    public  String getName() {
        return name;
    }

    public  void setName(String name) {
       this.name = name;
    }

    public int getId() {
        return id;
    }

    public  void setId(int id) {
        this.id = id;
    }

    public  String getDescription() {
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

    @Override // не забываем об аннотации
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
                '}';
    }


}
