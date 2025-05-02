package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo))
            .thenComparing(Task::getId)
    );
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    protected int generateId() {
        return ++id;
    }

    public void updateEpicTime(Epic epic) {
        List<SubTask> subTasksForEpic = epic.getSubTasksIds().stream()
                .map(subTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (subTasksForEpic.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }
        Duration totalDuration = subTasksForEpic.stream()
                .map(Task::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        Optional<LocalDateTime> earliestStart = subTasksForEpic.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        Optional<LocalDateTime> latestEnd = subTasksForEpic.stream()
                .map(st -> st.getStartTime() != null ? st.getStartTime().plus(st.getDuration()) : null)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo);

        epic.setDuration(totalDuration);
        epic.setStartTime(earliestStart.orElse(null));
        epic.setEndTime(latestEnd.orElse(null));
    }

    public void updateEpicFields(Epic epic) {
        updateStatusEpic(epic);
        updateEpicTime(epic);
    }

    public List<Task> getAllTasksAndSubtasks() {
        return Stream.concat(
                Stream.concat(tasks.values().stream(), subTasks.values().stream()),
                epics.values().stream()
        ).collect(Collectors.toList());
    }

    private boolean isAnyTaskOverlapping(Task newTask, Collection<Task> tasks) {
        for (Task existing : tasks) {
            if (existing.getId() == newTask.getId()) continue;
            if (existing.getStartTime() != null && newTask.getStartTime() != null &&
                    existing.getEndTime() != null && newTask.getEndTime() != null &&
                    !(newTask.getEndTime().isBefore(existing.getStartTime()) ||
                            newTask.getStartTime().isAfter(existing.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Task> getAllTasks() {
        if (tasks.size() == 0) {
            return Collections.emptyList();
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        if (epics.size() == 0) {
            return Collections.emptyList();
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        if (subTasks.size() == 0) {
            return Collections.emptyList();
        }
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subTasks.keySet().forEach(historyManager::remove);
        epics.keySet().forEach(historyManager::remove);
        subTasks.clear();
        epics.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subTasks.keySet().forEach(historyManager::remove);
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubTasksIds().clear();
            updateEpicFields(epic);
            prioritizedTasks.clear();
        });
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubtaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }


    @Override
    public int createTask(Task task) {
        if (isAnyTaskOverlapping(task, getAllTasksAndSubtasks())) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей!");
        }
        int newTaskId = generateId();
        task.setId(newTaskId);
        tasks.put(newTaskId, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return newTaskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = generateId();
        epic.setId(newEpicId);
        epics.put(newEpicId, epic);
        return newEpicId;
    }

    @Override
    public int createSubtask(SubTask subtask) {
        int newSubtaskId = generateId();
        subtask.setId(newSubtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            if (isAnyTaskOverlapping(subtask, getAllTasksAndSubtasks())) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей!");
            }
            subTasks.put(newSubtaskId, subtask);
            epic.setSubTasksIds(newSubtaskId);
            updateEpicFields(epic);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
            return newSubtaskId;
        } else {
            return -1;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId())) {
            return;
        }
        if (isAnyTaskOverlapping(task, getAllTasksAndSubtasks())) {
            System.out.println("Ошибка: Задача пересекается с другой задачей!");
            return;
        }
        Task oldTask = tasks.get(task.getId());
        prioritizedTasks.remove(oldTask);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        if (!subTasks.containsKey(subtask.getId())) return;
        SubTask old = subTasks.remove(subtask.getId());
        try {
            if (isAnyTaskOverlapping(subtask, getAllTasksAndSubtasks())) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей!");
            }
            Task oldsubTask = tasks.get(subtask.getId());
            prioritizedTasks.remove(oldsubTask);
            tasks.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            subTasks.put(subtask.getId(), old);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            epic.getSubTasksIds().forEach(subTasks::remove);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask subtask = subTasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubTasksIds().remove((Integer) subtask.getId());
            updateEpicFields(epic);
            subTasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(subtask);
        }
    }


    @Override
    public List<SubTask> getAllSubtasksEpic(int id) {
        Epic epic = epics.get(id);
        return epic != null
                ? epic.getSubTasksIds().stream()
                .map(subTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    protected void updateStatusEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Epic not found");
        }
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int countDone = 0;
        int countNew = 0;
        for (int subtaskId : epic.getSubTasksIds()) {
            SubTask subtask = subTasks.get(subtaskId);
            if (subtask == null) {
                continue;
            }

            if (subtask.getStatus() == Status.DONE) {
                countDone++;
            } else if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}