package main.java.taskmanager.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Task {

    private final UUID id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private boolean completed;
    private final List<User> assignedUsers;

    public Task(String title, String description, LocalDate deadline, Priority priority) {
        this(title, description, deadline, priority, new ArrayList<>());
    }

    public Task(String title, String description, LocalDate deadline, Priority priority, List<User> assignedUsers) {
        this.id =  UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = false;
        this.assignedUsers = new ArrayList<>(assignedUsers);
    }

    public UUID getId() {
        return id;
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

    public void assignUser(User user) {
        if (!assignedUsers.contains(user)) {
            assignedUsers.add(user);
        }
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    @Override
    public String toString() {
        String userNames = assignedUsers.stream()
                .map(user -> user.getName() + " " + user.getSurname())
                .collect(Collectors.joining(", "));

        return String.format(
            "Task: \nID: %s\nTitle: %s\nDescription: %s\nAssigned to: %s\nDeadline: %s\nPriority: %s\nStatus: %s\n",
            id,
            title,
            description,
            userNames.isEmpty() ? "Unassigned" : userNames,
            deadline,
            priority,
            completed ? "Completed" : "Pending"
        );
    }

}
