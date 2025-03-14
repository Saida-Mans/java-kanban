package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(description, name, status);
        this.epicId = epicId;
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
                '}';
    }
}