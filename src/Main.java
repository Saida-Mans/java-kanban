import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager=new TaskManager();
        taskManager.createTask(new Task("Имя1", "Описание1", Status.NEW));
        taskManager.createTask(new Task("Имя2", "Описание2", Status.NEW));
        taskManager.createTask(new Epic("Имя", "Описание", Status.NEW));
        taskManager.createSubtask(new SubTask("Имя2", "Описание2", Status.NEW, 1));
        taskManager.createSubtask(new SubTask("Имя", "Описание", Status.NEW, 1));

        System.out.println(taskManager);
    }
}
















