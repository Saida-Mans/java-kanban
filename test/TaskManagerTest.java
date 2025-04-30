import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest <T extends TaskManager>  {
    protected T manager;

    protected abstract T createManager();

    @BeforeEach
    public void setup() {
        manager = createManager();
    }

    @Test
    public void shouldCreateAndReturnTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 1, 10, 0));
        Task task1 = new Task("Test Task", "Test Description", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 1, 10, 0));
        int taskId = manager.createTask(task);
        int taskId1 = manager.createTask(task1);
        Task savedTask = manager.getTaskById(taskId);
        Task savedTask1 = manager.getTaskById(taskId1);
        assertNotNull(savedTask, "Задача не была сохранена");
        assertNotNull(savedTask1, "Задача не была сохранена");
        assertEquals("Test Task", savedTask.getName(), "Название задачи не совпадает");
        assertEquals(Duration.ofMinutes(60), savedTask.getDuration(), "Длительность не совпадает");
        assertEquals(Duration.ofMinutes(60), savedTask1.getDuration(), "Длительность не совпадает");
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), savedTask.getStartTime(), "Время старта не совпадает");
    }

    @Test
    public void shouldCreateAndReturnEpic() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW,
                Duration.ZERO, null);
        int epicId = manager.createEpic(epic);
        Epic savedEpic = manager.getEpicById(epicId);
        assertNotNull(savedEpic, "Эпик не был сохранен");
        assertEquals("Test Epic", savedEpic.getName());
    }

    @Test
    public void shouldCorrectlyLinkSubTaskToEpic() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW, Duration.ZERO, null);
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask Name", "SubTask Description", Status.NEW, epicId, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 11, 0));
        int subTaskId = manager.createSubtask(subTask);
        SubTask savedSubTask = manager.getSubtaskById(subTaskId);
        Epic savedEpic = manager.getEpicById(epicId);
        assertNotNull(savedSubTask, "Сабтаск не был сохранен");
        assertEquals("SubTask Name", savedSubTask.getName());
        assertEquals(Duration.ofMinutes(30), savedSubTask.getDuration());
        assertEquals(LocalDateTime.of(2025, 1, 1, 11, 0), savedSubTask.getStartTime());
        assertTrue(savedEpic.getSubTasksIds().contains(subTaskId), "Сабтаск не связан с эпиком");
    }

    @Test
    public void shouldCorrectlyCalculateEpicStatus() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.DONE, Duration.ZERO, null);
        int epicId = manager.createEpic(epic);
        SubTask sub1 = new SubTask("sub1", "desc", Status.DONE, epicId,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0));
        SubTask sub2 = new SubTask("sub2", "desc", Status.DONE, epicId,
                Duration.ofMinutes(30),  LocalDateTime.of(2023, 1, 1, 11, 0));
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);
        epic = manager.getEpicById(epicId);
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть NEW");
        sub1.setStatus(Status.DONE);
        sub2.setStatus(Status.DONE);
        manager.updateSubtask(sub1);
        manager.updateSubtask(sub2);
        epic = manager.getEpicById(epicId);
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void epicStatusShouldBeInProgressWhenSubtasksMixed() {
        Epic epic = new Epic("Epic 4", "Mixed subtasks", Status.NEW, Duration.ZERO, null);
        int epicId = manager.createEpic(epic);

        manager.createSubtask(new SubTask("sub1", "desc", Status.NEW, epicId,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.createSubtask(new SubTask("sub2", "desc", Status.DONE, epicId,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 11, 0)));

        Epic savedEpic = manager.getEpicById(epicId);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus());
    }

    @Test
    public void shouldCreateAndReturnSubTask() {
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW,
                Duration.ZERO, null);
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask Name", "SubTask Description", Status.NEW,
                epicId, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 11, 0));
        int subTaskId = manager.createSubtask(subTask);
        SubTask savedSubTask = manager.getSubtaskById(subTaskId);
        assertNotNull(savedSubTask, "Сабтаск не был сохранен");
        assertEquals("SubTask Name", savedSubTask.getName());
        assertEquals(Duration.ofMinutes(30), savedSubTask.getDuration());
        assertEquals(LocalDateTime.of(2025, 1, 1, 11, 0), savedSubTask.getStartTime());
    }

    @Test
    public void shouldNotAllowTaskTimeOverlap() {
        Task task1 = new Task(
                "Task 1",
                "Description 1",
                Status.NEW,
                Duration.ofMinutes(60),
                LocalDateTime.of(2023, 4, 1, 10, 0)
        );
        manager.createTask(task1);

        Task task2 = new Task(
                "Task 2",
                "Description 2",
                Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(2023, 4, 1, 10, 30)
        );
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.createTask(task2),
                "Expected conflict exception"
        );
        assertEquals("Задача пересекается с другой задачей!", exception.getMessage());
    }
}