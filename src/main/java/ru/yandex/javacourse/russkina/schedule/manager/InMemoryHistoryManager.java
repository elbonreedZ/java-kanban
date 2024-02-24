package ru.yandex.javacourse.russkina.schedule.manager;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> viewedTasks;

    InMemoryHistoryManager(){
        viewedTasks = new ArrayList<>();
    }


    @Override
    public ArrayList<Task> getHistory(){
        return viewedTasks;
    }

    @Override
    public void add(Task task){
        if (task instanceof Epic){
            Epic epic = (Epic) task;
            Epic newTask = new Epic(epic.getName(), epic.getDescription(), epic.getId());
            newTask.setStatus(epic.getStatus());
            newTask.setSubtasksId(epic.getSubtasksId());
            viewedTasks.add(newTask);
        } else if (task instanceof Subtask){
            Subtask subtask = (Subtask) task;
            Subtask newTask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getId(),
                    subtask.getEpicId(), subtask.getStatus());
            viewedTasks.add(newTask);
        } else {
            Task newTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());
            viewedTasks.add(newTask);
        }
        updateViewedTasks();

    }

    private void updateViewedTasks(){
        if(viewedTasks.size() > 10){
            viewedTasks.remove(0);
        }
    }

}
