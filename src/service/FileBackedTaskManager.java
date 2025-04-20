package service;
import model.TaskType;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;
    private int currentId = 0;

    public  FileBackedTaskManager(HistoryManager historyManager, Path path) {
        super(historyManager);
        try {
            if (!Files.exists(path)) {
                path = Paths.get("java-kanban/service", "testFile.csv");
                Files.createFile(path);
            }
            this.path = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                sb.append(toString(task)).append("\n");
            }
            for (Epic epic : epics.values()) {
                sb.append(toString(epic)).append("\n");
            }
            for (SubTask subtask : subTasks.values()) {
                sb.append(toString(subtask)).append("\n");
            }

            Files.writeString(path, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    private String toString(Task task) {
        String base = String.format("%d,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription()
        );

        if (task instanceof SubTask) {
            return base + "," + ((SubTask) task).getEpicId();
        }
        return base;
    }

    public static Task fromString(String value) {
        String[] line = value.split(",");
        int id = Integer.parseInt(line[0]);
        TaskType type = TaskType.valueOf(line[1]);
        String name = line[2];
        Status status = Status.valueOf(line[3]);
        String description = line[4];
        Task task;
        switch (type) {
            case TASK:
                task = new Task(name, description, status);
                task.setId(id);
                return task;

            case EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;

            case SUBTASK:
                int epicId = Integer.parseInt(line[5]);
                SubTask subtask = new SubTask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
        }
        throw new ManagerSaveException("Нет нужного типа ");
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(new InMemoryHistoryManager(), path);
        int maxId=0;
        try {
            String content = Files.readString(path);
            String[] arrayStrings = content.split("\n");
            int count = 0;
            for (String line : arrayStrings) {
                if (count++ == 0 || line.isBlank()) {
                    continue;
                }
                Task task = fromString(line);
                maxId= Math.max(maxId, task.getId());
                switch (task.getType()) {
                    case TASK -> manager.tasks.put(task.getId(), task);
                    case EPIC -> manager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> {
                        SubTask subtask = (SubTask) task;
                        manager.subTasks.put(task.getId(), subtask);
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            epic.setSubTasksIds(subtask.getId());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        manager.currentId=maxId;
        return manager;
    }

    @Override
    protected int generateId() {
        return ++currentId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return currentId;
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return currentId;
    }

    @Override
    public int createSubtask(SubTask subtask) {
        super.createSubtask(subtask);
        save();
        return currentId;
    }
}