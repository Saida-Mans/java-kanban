import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    public File tempFile;
    public Path path;

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(path);
    }

    @Override
    protected FileBackedTaskManager createManager() {
        try {
            tempFile = File.createTempFile("test", ".csv");
            path = tempFile.toPath();
            return new FileBackedTaskManager(Managers.getDefaultHistory(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        Task task1 = new Task("Test Task", "Test Task description", Status.NEW);
        Epic epic1 = new Epic("Test Epic", "Test Epic description", Status.NEW);
        int taskId = manager.createTask(task1);
        int epicId = manager.createEpic(epic1);
        SubTask subTask = new SubTask("SubTask Name", "SubTask Description", Status.NEW, epicId);
        int subTaskId = manager.createSubtask(subTask);
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(path);
        Task loadedTask = loadedManager.getTaskById(taskId);
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        SubTask savedSubTask = loadedManager.getSubtaskById(subTaskId);
        assertNotNull(loadedTask, "Задача не загрузилась");
        assertNotNull(loadedEpic, "Эпик не загрузился");
        assertNotNull(savedSubTask, "Сабтаск не был сохранен");
        List<Task> history = loadedManager.getHistory();
        assertEquals(3, history.size(), "История задач восстановилась некорректно");
    }
}
