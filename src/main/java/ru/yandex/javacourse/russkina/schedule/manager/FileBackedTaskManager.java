package ru.yandex.javacourse.russkina.schedule.manager;


import ru.yandex.javacourse.russkina.schedule.exception.ManagerLoadException;
import ru.yandex.javacourse.russkina.schedule.exception.ManagerSaveException;
import ru.yandex.javacourse.russkina.schedule.task.Epic;
import ru.yandex.javacourse.russkina.schedule.task.Subtask;
import ru.yandex.javacourse.russkina.schedule.task.Task;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void setEpicId(int epicId, Subtask subtask) {
        super.setEpicId(epicId, subtask);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine();
                boolean isHistory = false;
                while (br.ready()) {
                    line = br.readLine();
                    if (line.isBlank()) {
                        isHistory = true;
                        continue;
                    }
                    if (!isHistory) {
                        Task task = CVSTaskFormat.fromString(line);
                        taskManager.restoreTaskToManager(task);
                    } else {
                        List<Integer> history = CVSTaskFormat.historyFromString(line);
                        taskManager.restoreTasksToHistory(history);
                    }
                }
            } catch (IOException e) {
                throw new ManagerLoadException("Ошибка загрузки файла");
            }
        } catch (ManagerLoadException e) {
            System.out.println(e.getMessage());
        }
        return taskManager;
    }

    private void save() {
        List<Task> tasks = getTasks();
        List<Epic> epics = getEpics();
        List<Subtask> subtasks = getSubtasks();
        try {
            try (Writer fileWriter = new FileWriter(file)) {
                fileWriter.write(CVSTaskFormat.getHeader());
                fileWriter.write("\n");
                for (Task task : tasks) {
                    fileWriter.write(CVSTaskFormat.toString(task) + "\n");
                }
                for (Epic epic : epics) {
                    fileWriter.write(CVSTaskFormat.toString(epic) + "\n");
                }
                for (Subtask subtask : subtasks) {
                    fileWriter.write(CVSTaskFormat.toString(subtask) + "\n");
                }
                fileWriter.write("\n");
                fileWriter.write(CVSTaskFormat.historyToString(super.historyManager));
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка сохранения файла");
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private void restoreTasksToHistory(List<Integer> history) {
        for (int id : history) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
            }
        }
    }

    private void restoreTaskToManager(Task task) {
        if (task instanceof Subtask) {
            super.subtasks.put(task.getId(), (Subtask) task);
            super.addSubtaskId((Subtask) task);
        } else if (task instanceof Epic) {
            super.epics.put(task.getId(), (Epic) task);
        } else {
            super.tasks.put(task.getId(), task);
        }
        if (task.getId() > super.getTaskIdCounter()) {
            super.setTaskIdCounter(task.getId());
        }
    }
}
