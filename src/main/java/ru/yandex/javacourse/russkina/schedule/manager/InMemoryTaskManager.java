package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>((t1, t2) -> {
        int result = t1.getStartTime().compareTo(t2.getStartTime());
        if (result == 0) {
            return t1.getId() - t2.getId();
        } else {
            return result;
        }
    });
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    private int taskIdCounter;

    public int getTaskIdCounter() {
        return taskIdCounter;
    }

    public void setTaskIdCounter(int taskIdCounter) {
        this.taskIdCounter = taskIdCounter;
    }

    public InMemoryTaskManager() {
        taskIdCounter = 0;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateTaskId());
        Task finalTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                task.getDuration(), task.getStartTime());
        if (isTasksOverlapStream(finalTask)) {
            return null;
        }
        tasks.put(finalTask.getId(), finalTask);
        return finalTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateTaskId());
        Epic finalTask = new Epic(epic.getName(), epic.getDescription(), epic.getId());
        epics.put(finalTask.getId(), finalTask);
        return finalTask;
    }


    @Override
    public Subtask createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.get(epicId) == null) {
            return null;
        }
        subtask.setId(generateTaskId());
        Subtask finalTask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId(),
                subtask.getEpicId(), subtask.getStatus(), subtask.getDuration(), subtask.getStartTime());
        if (isTasksOverlapStream(finalTask)) {
            return null;
        }
        subtasks.put(finalTask.getId(), finalTask);
        addSubtaskId(finalTask);
        updateEpic(epicId);
        return finalTask;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            updateEpic(epic.getId());
        }
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.remove(id);
        if (isTasksOverlapStream(task)) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasksId(savedEpic.getSubtasksId());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        if (isTasksOverlapStream(subtask)) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpic(epicId);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        List<Integer> subtasksId = epic.getSubtasksId();
        subtasksId = subtasksId.stream().filter(subtaskId -> subtaskId != id).collect(Collectors.toList());
        epic.setSubtasksId(subtasksId);
        updateEpic(epic.getId());
        historyManager.remove(id);
    }


    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getSubtasksId().stream()
                .map(subtasks::get).collect(Collectors.toList());
    }

    @Override
    public void setEpicId(int epicId, Subtask subtask) {
        Epic oldEpic = epics.get(subtask.getEpicId());
        subtask.setEpicId(epicId);
        addSubtaskId(subtask);
        int indexOfSub = -1;
        for (int i = 0; i < oldEpic.getSubtasksId().size(); i++) {
            if (oldEpic.getSubtasksId().get(i) == subtask.getId()) {
                indexOfSub = i;
            }
        }
        if (indexOfSub != -1) {
            oldEpic.getSubtasksId().remove(indexOfSub);
            updateEpic(epicId);
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        prioritizedTasks.addAll(getTasks().stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList()));
        prioritizedTasks.addAll(getEpics().stream()
                .filter(epic -> epic.getStartTime() != null)
                .collect(Collectors.toList()));
        prioritizedTasks.addAll(getSubtasks().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .collect(Collectors.toList()));
        return prioritizedTasks;
    }

    protected void updateEpic(int epicId) {
        updateEpicStatus(epicId);
        updateEpicDurationAndTime(epicId);
    }

    private boolean isTasksOverlapStream(Task newTask) {
        if (newTask.getStartTime() == null) {
            return false;
        }
        try {
            Optional<Task> crossedTask = getPrioritizedTasks().stream()
                    .filter(task -> !task.getType().equals(Type.EPIC))
                    .filter(task -> task.getId() != newTask.getId())
                    .filter(streamTask -> isTasksOverlap(newTask, streamTask))
                    .findFirst();
            if (crossedTask.isPresent()) {
                throw new TaskValidationException(
                        "Задача не может быть добавлена/обновлена, она пересекается с id = " + crossedTask.get().getId()
                                + ", время начала: " + crossedTask.get().getStartTime() +
                                ", время окончания: " + crossedTask.get().getEndTime()
                );
            }
        } catch (TaskValidationException e) {
                System.out.println(e.getMessage());
                return true;
        }
        return false;
    }

    private boolean isTasksOverlap(Task task1, Task task2) {
        boolean isEndTimeBefore = task1.getEndTime().isBefore(task2.getStartTime());
        boolean isStartTimeAfter = task1.getStartTime().isAfter(task2.getEndTime());
        return !(isEndTimeBefore || isStartTimeAfter);
    }

    private void updateEpicDurationAndTime(int epicId) {
        Epic epic = epics.get(epicId);
        long duration = 0;
        LocalDateTime minDateTime = LocalDateTime.MAX;
        LocalDateTime maxDateTime = LocalDateTime.MIN;
        List<Subtask> subtaskList = epic.getSubtasksId().stream()
                .map(subtasks::get)
                .filter(subtask -> subtask.getStartTime() != null).collect(Collectors.toList());
        for (Subtask subtask : subtaskList) {
            duration += subtask.getDuration();
            if (subtask.getStartTime().isBefore(minDateTime)) {
                minDateTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(maxDateTime)) {
                maxDateTime = subtask.getEndTime();
            }
        }
        epic.setDuration(duration);
        if (minDateTime != LocalDateTime.MAX) {
            epic.setStartTime(minDateTime);
            epic.setEndTime(maxDateTime);
        }
    }

    private int generateTaskId() {
        return ++taskIdCounter;
    }

    protected void addSubtaskId(Subtask subtask) {
        if (subtask.getId() == subtask.getEpicId()) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().add(subtask.getId());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        Epic updatedEpic = new Epic(epic.getName(), epic.getDescription(), epicId);
        updatedEpic.setSubtasksId(epic.getSubtasksId());
        if (getEpicSubtasks(epicId).isEmpty()) {
            updatedEpic.setStatus(Status.NEW);
            epics.put(epicId, updatedEpic);
            return;
        }
        int counter = 0;
        for (Subtask subtask : getEpicSubtasks(epicId)) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                updatedEpic.setStatus(Status.IN_PROGRESS);
                epics.put(epicId, updatedEpic);
                return;
            } else if (subtask.getStatus() == Status.DONE) {
                counter++;
            }
        }
        if (counter == getEpicSubtasks(epicId).size()) {
            updatedEpic.setStatus(Status.DONE);
            epics.put(epicId, updatedEpic);
        } else if (counter == 0) {
            updatedEpic.setStatus(Status.NEW);
            epics.put(epicId, updatedEpic);
        } else {
            updatedEpic.setStatus(Status.IN_PROGRESS);
            epics.put(epicId, updatedEpic);
        }

    }


}

