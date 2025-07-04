package taskmanager.service;

import org.jetbrains.annotations.NotNull;
import taskmanager.model.Priority;
import taskmanager.model.Task;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<Task>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void listTasks() {
        if(tasks.isEmpty()) {
            System.out.println("No task available");
        }

        IntStream.range(0, tasks.size())
                .forEach(i -> System.out.println("\n" + (i + 1) + ". " + tasks.get(i)));
    }

    public void markTaskAsCompleted(UUID id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.markAsCompleted();
                System.out.println("Task marked as completed.");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public boolean editTask(UUID id, String newTitle, String newDescription, LocalDate newDeadline, Priority newPriority) {
        for (Task task : tasks) {
            if (!task.getId().equals(id)) {
                continue;
            }

            if (newTitle != null && !newTitle.isBlank()) task.setTitle(newTitle);
            if (newDescription != null && !newDescription.isBlank()) task.setDescription(newDescription);
            if (newDeadline != null) task.setDeadline(newDeadline);
            if (newPriority != null) task.setPriority(newPriority);

            return true;
        }

        return false;
    }

    public Task getTaskById(UUID id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteTask(UUID id) {
        return tasks.removeIf(task -> task.getId().equals(id));
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream().filter(task -> task.isCompleted())
                .collect(Collectors.toList());
    }

    public List<Task> findTasksByTitle(String searchTerm) {
        String normalizedSearchTerm = normalize(searchTerm);

        return tasks.stream()
                .filter(task -> {
                    String normalizedTitle = normalize(task.getTitle());
                    return normalizedTitle.contains(normalizedSearchTerm);
                })
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        return tasks.stream()
                .filter(task -> task.isOverdue())
                .collect(Collectors.toList());
    }

    private @NotNull String normalize(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT);
    }

}
