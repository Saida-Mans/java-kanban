package service;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubtasks();
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById(int id);
    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<SubTask> getAllSubtasksEpic(int id);

    List<Task> getHistory();

}
