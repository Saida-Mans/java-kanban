import model.Epic;
import model.Status;
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


import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    public File tempFile;
    public Path path;
    public FileBackedTaskManager fileBackedTaskManager;


    @BeforeEach
    public void beforeEach() {
        try {
            tempFile = File.createTempFile("test", ".csv");
            path = tempFile.toPath();
            fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        // Создаём задачи
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", NEW);
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
        int taskId = fileBackedTaskManager.createTask(task1);
        int epicId = fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(path);
        Task loadedTask = loadedManager.getTaskById(taskId);
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedTask, "Задача не загрузилась");
        assertNotNull(loadedEpic, "Эпик не загрузился");
        List<Task> history = loadedManager.getHistory();
        assertEquals(2, history.size(), "История задач восстановилась некорректно");
    }
}