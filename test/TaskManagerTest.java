package test;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import java.util.List;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    static TaskManager taskManager;

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertNotEquals(0,
                taskManager.getHistory().size(),
                "После операций с задачами (Task) история не должна быть пустой!");

    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Test addEpic", "Test addEpic description", NEW);
        final int epicId = taskManager.createTask(epic);

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

    }

    @Test
    void addSubtask() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        int epicId = taskManager.createEpic(new Epic("Имя", "Описание", Status.NEW));
        System.out.println("epicId = " + epicId);
        SubTask subTask = new SubTask("Имя2", "Описание2", Status.NEW, epicId);

        int subtaskId = taskManager.createSubtask(subTask);
        System.out.println("subtaskId = " + subtaskId);

        SubTask subTask1 = taskManager.getSubtaskById(subtaskId);
        System.out.println("subTask = " + subTask);
        System.out.println("subTask1 = " + subTask1);
        assertEquals(subTask, subTask1, "Наследники класса Task не равны друг другу");
    }


    @Test
    void addEpicToEpic() {
        assertThrows(RuntimeException.class, () -> {
            TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
            Epic epic = new Epic("Эпик-1", "Описание-2", NEW);

            int epicId = taskManager.createEpic(epic);

            epic.setSubTasksIds(epicId);
        });
    }

    @Test
    void addSubtaskAsEpic() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        Epic epic = new Epic("Эпик-1", "Описание-2", NEW);
        int epicId = taskManager.createEpic(epic);
        epic.setSubTasksIds(epicId);

        SubTask subTask = new SubTask("Имя2", "Описание2", Status.NEW, epicId);

    }

    @Test
    public void deleteAllTasks() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        Task task = new Task("Задача", "Описание-1", NEW);
        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void deleteAllEpics() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        Epic epic = new Epic("Эпик-1", "Описание-1", NEW);
        taskManager.createEpic(epic);
        taskManager.deleteAllEpics();
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size());
    }

    @Test
    public void deleteAllSubtask() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        SubTask subTask = new SubTask("Подзадача-1", "Описани-2", NEW, 3);
        taskManager.createSubtask(subTask);
        taskManager.deleteAllSubtasks();
        List<SubTask> subtasks = taskManager.getAllSubtasks();
        assertEquals(0, subtasks.size());
    }


    @Test
    public void showHistory() {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        taskManager.createTask(new Task("Описание-1", "Task-1", Status.NEW));
        taskManager.createTask(new Task("Описание-2", "Task-2", Status.NEW));
        taskManager.createEpic(new Epic("Описание-1", "Epic-1", Status.NEW));
        taskManager.createEpic(new Epic("Описание-1", "Epic-2", Status.NEW));
        taskManager.createSubtask(new SubTask("Описание-1", "Subtask-1", Status.NEW, 3));
        taskManager.createSubtask(new SubTask("Описание-2", "Subtask-2", Status.NEW, 3));
        taskManager.createSubtask(new SubTask("Описание-3", "Subtask-3", Status.NEW, 3));

        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        List<Task> history = taskManager.getHistory();
        System.out.println(history);
    }
}












