import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
public class TaskManager {
    private static int id = 0;
    HashMap<Integer, Task> tasks= new HashMap<>();
    HashMap<Integer, Epic> epics= new HashMap<>();
    HashMap<Integer, SubTask> subTasks= new HashMap<>();

    public int generateId() {
        return ++id;
    }


    public List<Task> getAllTasks() {
        if (tasks.size() == 0) {
            System.out.println("Список задач пуст");
            return Collections.emptyList();
        }
        return new ArrayList<>(tasks.values());
    }


    public List<Epic> getAllEpics() {
        if (epics.size() == 0) {
            System.out.println("Список эпиков пуст");
            return Collections.emptyList();
        }
        return new ArrayList<>(epics.values());
    }
    public List<SubTask> getAllSubtasks() {
        if (subTasks.size() == 0) {
            System.out.println("Список подзадач пуст");
            return Collections.emptyList();
        }
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subTasks.clear();
        epics.clear();
    }


    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksIds().clear();
            updateStatusEpic(epic);
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }


    public Epic getEpicById(int id) {
        return epics.get(id);
    }


    public SubTask getSubtaskById(int id) {
        return subTasks.get(id);
    }


    public int createTask(Task task) {
        int newTaskId = generateId();
        task.setId(newTaskId);
        tasks.put(newTaskId, task);
        return newTaskId;
    }

    public int createEpic(Epic epic) {
        int newEpicId = generateId();
        epic.setId(newEpicId);
        epics.put(newEpicId, epic);
        return newEpicId;
    }


    public int createSubtask(SubTask subtask) {
        int newSubtaskId = generateId();
        subtask.setId(newSubtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subTasks.put(newSubtaskId, subtask);
            epic.setSubTasksIds(newSubtaskId);
            updateStatusEpic(epic);
            return newSubtaskId;
        } else {
            System.out.println("Эпиков не нашел");
            return -1;
        }
    }


    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задач не нашел");
        }
    }


    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
        } else {
            System.out.println("Эпиков не нашел");
        }
    }

    public void updateSubtask(SubTask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateStatusEpic(epic);
        } else {
            System.out.println("Подзадач не нашел");
        }
    }


    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задач не нашел");
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubTasksIds()) {
                subTasks.remove(subtaskId);
            }
            epics.remove(id);
        } else {
            System.out.println("Эпиков не нашел");
        }
    }


    public void deleteSubtaskById(int id) {
        SubTask subtask = subTasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubTasksIds().remove((Integer) subtask.getId());
            updateStatusEpic(epic);
            subTasks.remove(id);
        } else {
            System.out.println("Подзадач не нашел");
        }
    }


    public List<SubTask> getAllSubtasksEpic(int id) {
        if (epics.containsKey(id)) {
            List<SubTask> subNew = new ArrayList<>();
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                subNew.add(subTasks.get(epic.getSubTasksIds().get(i)));
            }
            return subNew;
        } else {
            return Collections.emptyList();
        }
    }


    public void updateStatusEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            if (epic.getSubTasksIds().size() == 0) {
                epic.setStatus(Status.NEW);
            } else {
                List<SubTask> subtasksNew = new ArrayList<>();
                int countDone = 0;
                int countNew = 0;

                for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                    subtasksNew.add(subTasks.get(epic.getSubTasksIds().get(i)));
                }

                for (SubTask subtask : subtasksNew) {
                    if (subtask.getStatus() == Status.DONE) {
                        countDone++;
                    }
                    if (subtask.getStatus() == Status.NEW) {
                        countNew++;
                    }
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }

                if (countDone == epic.getSubTasksIds().size()) {
                    epic.setStatus(Status.DONE);
            } else if (countNew == epic.getSubTasksIds().size()) {
                    epic.setStatus(Status.NEW);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        } else {
            System.out.println("Эпиков не нашел");
        }
    }
}