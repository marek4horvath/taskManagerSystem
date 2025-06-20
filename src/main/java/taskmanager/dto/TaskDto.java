package taskmanager.dto;

import taskmanager.model.Priority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TaskDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Priority priority;
    private List<UUID> assignedUsers;

    public TaskDto() {

    }

    public TaskDto(String title, String description, LocalDateTime deadline, Priority priority, List<UUID> assignedUsers) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.assignedUsers = assignedUsers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<UUID> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UUID> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

}
