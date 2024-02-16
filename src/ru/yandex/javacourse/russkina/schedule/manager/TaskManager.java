package ru.yandex.javacourse.russkina.schedule.manager;

import ru.yandex.javacourse.russkina.schedule.task.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int taskIdCounter;

    public TaskManager() {
        taskIdCounter = 0;
    }

    public Task createTask(Task task) {
        task.setId(generateTaskId());
        Task finalTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());
        tasks.put(finalTask.getId(), finalTask);
        return finalTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateTaskId());
        Epic finalTask = new Epic(epic.getName(), epic.getDescription(), epic.getId());
        epics.put(finalTask.getId(), finalTask);
        return finalTask;
    }


    public Subtask createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        subtask.setId(generateTaskId());
        Subtask finalTask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId(),
                subtask.getEpicId(), subtask.getStatus());
        subtasks.put(finalTask.getId(), finalTask);
        addSubtaskId(finalTask);
        updateEpicStatus(epicId);
        return finalTask;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() { // для чего нужен список всех подзадач, отдельно от эпика?
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()){
            epic.getSubtasksId().clear();
        }
    }
    public void removeTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = getTaskById(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = getEpicById(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtasksId(savedEpic.getSubtasksId());
        updateEpicStatus(epic.getId());
        epics.put(epic.getId(), epic); // на всякий случай цитирую из тз:  обновление — это запись нового эпика
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = getSubtaskById(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = getEpicById(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
    }
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subtasksId= epic.getSubtasksId();
        int indexOfSub = -1;
        for (int i = 0; i < subtasksId.size(); i++){
            if (subtasksId.get(i) == id) {
                indexOfSub = i;
            }
        }
        if (indexOfSub != -1){
            subtasksId.remove(indexOfSub);
        }
        updateEpicStatus(epic.getId());
    }


    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtasksId()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

    public void printTest(){
        System.out.println("Задачи:");
        for (Task task : getTasks()) {
            System.out.print(task.getName() + " id = " + task.getId() + ": ");
            System.out.println(task.getStatus());
        }
        System.out.println();
        for (Epic epic : getEpics()) {
            System.out.println("Эпик:");
            System.out.print(epic.getName() + " id = " + epic.getId() + ": ");
            System.out.println(epic.getStatus());
            System.out.println("Подзадачи эпика:");
            if(getEpicSubtasks(epic.getId()).isEmpty()){
                System.out.println("Нет");
            }
            for (Subtask subtask : getEpicSubtasks(epic.getId())) {
                System.out.print(subtask.getName() + " id = " + subtask.getId() + ": ");
                System.out.println(subtask.getStatus());
            }
            System.out.println();
        }
        System.out.println("Конец вывода");
        System.out.println();
    }

    private int generateTaskId() {
        return ++taskIdCounter;
    }

    private void addSubtaskId(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasksId().add(subtask.getId());
    }

    private void updateEpicStatus(int epicId){
        Epic epic = epics.get(epicId);
        if (getEpicSubtasks(epicId).isEmpty()){
            epic.setStatus(Status.NEW);
            return;
        }
        int counter = 0;
        for (Subtask subtask: getEpicSubtasks(epicId)){
            if (subtask.getStatus() == Status.IN_PROGRESS){
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtask.getStatus() == Status.DONE){
               counter++;
            }
        }
        if (counter == getEpicSubtasks(epicId).size()){
            epic.setStatus(Status.DONE);
        }else if (counter == 0){
            epic.setStatus(Status.NEW);
        }

    }

}

