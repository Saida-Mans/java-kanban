package service;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id) throws NotFoundException;

    Epic getEpicById(int id);

    SubTask getSubtaskById(int id) throws NotFoundException;

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(SubTask subtask);

    void updateTask(Task task) throws NotFoundException;

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<SubTask> getAllSubtasksEpic(int id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}
