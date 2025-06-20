package taskmanager.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanager.dto.TaskDto;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.repository.TaskRepository;
import taskmanager.repository.UserRepository;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(@NotNull TaskDto taskDto) {
        List<User> assignedUsers = new ArrayList<>();

        if (taskDto.getAssignedUsers() != null) {
            for (UUID userId : taskDto.getAssignedUsers()) {
                Optional<User> userOpt = userRepository.findById(userId);
                userOpt.ifPresent(assignedUsers::add);
            }
        }

        Task task = new Task(
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getDeadline(),
                taskDto.getPriority(),
                assignedUsers
        );

        for (User user : assignedUsers) {
            user.getTasks().add(task);
        }

        return  taskRepository.save(task);
    }


    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public boolean deleteTask(UUID id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean markAsCompleted(UUID id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task t = task.get();
            t.setCompleted(true);
            taskRepository.save(t);
            return true;
        }
        return false;
    }

    public boolean updateTask(UUID id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setDeadline(taskDto.getDeadline());
            task.setPriority(taskDto.getPriority());
            taskRepository.save(task);
            return true;
        }
        return false;
    }

//    public List<Task> getOverdueTasks() {
//        // Predpokladám, že metóda isOverdue() existuje v entite Task
//        return taskRepository.findAll().stream()
//                .filter(Task::isOverdue)
//                .collect(Collectors.toList());
//    }

    public List<Task> findTasksByTitle(String searchTerm) {
        String normalizedSearchTerm = normalize(searchTerm);

        return taskRepository.findAll().stream()
                .filter(task -> {
                    String normalizedTitle = normalize(task.getTitle());
                    return normalizedTitle.contains(normalizedSearchTerm);
                })
                .collect(Collectors.toList());
    }

    private @NotNull String normalize(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT);
    }
}
