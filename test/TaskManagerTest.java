import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public abstract class TaskManagerTest <T extends TaskManager>  {
    protected T manager;

    protected abstract T createManager();

    @BeforeEach
    public void setup() {
        manager = createManager();
    }

    @Test
    public void shouldCreateAndReturnTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        int taskId = manager.createTask(task);

        Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не была сохранена");
        assertEquals("Test Task", savedTask.getName(), "Название задачи не совпадает");

    }

    @Test
    public void shouldCreateAndReturnEpic() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW);
        int epicId = manager.createEpic(epic);
        Epic savedEpic = manager.getEpicById(epicId);
        assertNotNull(savedEpic, "Эпик не был сохранен");
        assertEquals("Test Epic", savedEpic.getName());
    }

    @Test
    public void shouldCreateAndReturnSubTask() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW);
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask Name", "SubTask Description", Status.NEW, epicId);
        int subTaskId = manager.createSubtask(subTask);
        SubTask savedSubTask = manager.getSubtaskById(subTaskId);
        assertNotNull(savedSubTask, "Сабтаск не был сохранен");
        assertEquals("SubTask Name", savedSubTask.getName());

    }
}