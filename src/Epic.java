import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Task> subtasks;

    public HashMap<Integer, Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Task> subtasks) {
        this.subtasks = subtasks;
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
        subtasks = new HashMap<>();
    }


}
