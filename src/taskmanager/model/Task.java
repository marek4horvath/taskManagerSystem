package taskmanager.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {

    private UUID id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private boolean completed;

    public Task(String title, String description, LocalDate deadline, Priority priority) {
        this.id =  UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public boolean isOverdue () {
        return !completed && deadline.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {

        return String.format(
            "Task: \nID: %s\nTitle: %s\nDescription: %s\nDue: %s\nPriority: %s\nStatus: %s\n",
            id,
            title,
            description,
            deadline,
            priority,
            completed ? "Completed" : "Pending"
        );
    }

}
