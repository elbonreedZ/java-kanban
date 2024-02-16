import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    private static int taskIdCounter;

    public TaskManager() {
        taskIdCounter = 0;
    }

    public int generateTaskId() {
        return ++taskIdCounter;
    }

    public void createTask(Task task) {
        task.setId(generateTaskId());
        Task finalTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());
        tasks.put(finalTask.getId(), finalTask);
    }

    public void createEpic(Epic task) {
        task.setId(generateTaskId());
        Epic finalTask = new Epic(task.getName(), task.getDescription(), task.getId());
        tasks.put(finalTask.getId(), finalTask);
        epics.put(finalTask.getId(), finalTask);
    }

    private void addSubtask(Subtask task) {
        if (tasks.containsKey(task.getEpicId())) {
            Epic epic = (Epic) tasks.get(task.getEpicId());
            epic.getSubtasks().put(task.getId(), task);
        }
    }

    public void createSubtask(Subtask task) {
        task.setId(generateTaskId());
        Subtask finalTask = new Subtask(task.getName(), task.getDescription(), task.getId(), task.getEpicId(), task.getStatus());
        tasks.put(finalTask.getId(), finalTask);
        addSubtask(finalTask);
    }

    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
    }

    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic task) {
        Epic oldEpic = (Epic) tasks.get(task.getId());
        task.setStatus(oldEpic.getStatus());
        task.setSubtasks(oldEpic.getSubtasks());
        tasks.put(task.getId(), task);
        epics.put(task.getId(), task);
    }

    public void updateSubtask(Subtask task) {
        tasks.put(task.getId(), task);
        addSubtask(task);
        Epic epic = (Epic) tasks.get(task.getEpicId());
        if (task.getStatus() == Status.IN_PROGRESS) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (task.getStatus() == Status.DONE) {
            int counter = 0;
            for (Task subtask : epic.getSubtasks().values()) {
                if (subtask.getStatus() == Status.NEW || subtask.getStatus() == Status.IN_PROGRESS) {
                    counter++;
                }
            }
            if (counter == 0) {
                epic.setStatus(Status.DONE);
            }
        }
    }

    public void removeById(int id) {
        tasks.remove(id);
        Task subtask;
        for (Epic epic : epics.values()) {
            subtask = epic.getSubtasks().get(id);
            if (subtask != null && subtask.getId() == id) {
                epic.getSubtasks().remove(id);
            }
        }

    }

    public void removeEpicById(int id) {
        int subId;
        Epic epic = epics.get(id);
        for (Task subtask : epic.getSubtasks().values()) {
            subId = subtask.getId();
            tasks.remove(subId);
        }
        epics.remove(id);
        tasks.remove(id);
    }


    public ArrayList<Task> getSubtasksList(int epicId) {
        Epic epic = (Epic) tasks.get(epicId);
        return new ArrayList<>(epic.getSubtasks().values());
    }

}

