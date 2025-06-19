package main.java.taskmanager.ui;

import main.java.taskmanager.model.Task;
import main.java.taskmanager.model.Priority;
import main.java.taskmanager.model.User;
import main.java.taskmanager.service.TaskManager;
import main.java.taskmanager.service.UserManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleUI {

    private final Scanner scanner;
    private final TaskManager taskManager;
    private final UserManager userManager;

    public ConsoleUI(Scanner scanner, TaskManager taskManager, UserManager userManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
        this.userManager = userManager;
    }

    private String mainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Task Management");
        System.out.println("2. User Management");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
        return scanner.nextLine();
    }

    private String taskManagement() {
        System.out.println("\n--- TASK MANAGEMENT ---");
        System.out.println("1. Add task");
        System.out.println("2. Show all tasks");
        System.out.println("3. Edit task");
        System.out.println("4. Assign task");
        System.out.println("5. Mark task as completed");
        System.out.println("6. Delete task");
        System.out.println("7. Find tasks by title");
        System.out.println("0. Back");
        System.out.print("Choose option: ");

        return scanner.nextLine();
    }

    private String userManagement() {
        System.out.println("\n--- USER MANAGEMENT ---");
        System.out.println("1. Create user");
        System.out.println("2. Show all users");
        System.out.println("0. Back");
        System.out.print("Choose option: ");

        return scanner.nextLine();
    }

    private void taskMenu() {
        while (true) {

            String choice = taskManagement();

            switch (choice) {
                case "1":
                    Task newTask = createTaskFromInput();
                    taskManager.addTask(newTask);
                    System.out.println("Task added.");
                    break;
                case "2":
                    taskManager.listTasks();
                    break;
                case "3":
                    editTaskFromInput();
                    break;
                case "4":
                    assignTask();
                    break;
                case "5":
                    markAsCompletedFromInput();
                    break;
                case "6":
                    deleteTask();
                    break;
                case "7":
                    findTasksByTitle();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void userMenu() {
        while (true) {

            String choice = userManagement();

            switch (choice) {
                case "1":
                    User user = createUserFromInput();
                    userManager.addUser(user);
                    System.out.println("User created: " + user);
                    break;
                case "2":
                    // TOTO list user
                    //userManager.listUsers();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    public void run() {
        while (true) {
            String choice = mainMenu();

            switch (choice) {
                case "1":
                    taskMenu();
                    break;
                case "2":
                    userMenu();
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    @Contract(" -> new")
    private @NotNull Task createTaskFromInput() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        LocalDate dueDate;
        while (true) {
            System.out.print("Enter due date (yyyy-mm-dd): ");
            String input = scanner.nextLine();
            try {
                dueDate = LocalDate.parse(input);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }

        Priority priority;
        while (true) {
            String priorities = String.join(" / ",
                    Arrays.stream(Priority.values())
                            .map(Enum::name)
                            .toArray(String[]::new)
            );

            System.out.print("Enter priority (" + priorities + "): ");

            try {
                priority = Priority.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority.");
            }
        }

        System.out.print("Enter user IDs to assign to the task (comma-separated): ");
        String input = scanner.nextLine();
        String[] idUsers = input.split(",");

        List<User> assignedUsers = new ArrayList<>();

        for (String idUser : idUsers) {
            try {
                UUID id = UUID.fromString(idUser.trim());
                User userOpt = userManager.getUserById(id);

                if (userOpt != null) {
                    assignedUsers.add(userOpt);
                } else {
                    System.out.println("User not found with ID: " + idUser.trim());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID: " + idUser.trim());
            }
        }

        return new Task(title, description, dueDate, priority, assignedUsers);
    }

    @Contract(" -> new")
    private @NotNull User createUserFromInput() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();

        System.out.print("Enter user surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter user password: ");
        String password = scanner.nextLine();

        return new User(name, surname, password);
    }

    private void editTaskFromInput() {
        System.out.print("Enter task ID to edit: ");
        UUID editId = UUID.fromString(scanner.nextLine());

        Task taskToEdit = taskManager.getTaskById(editId);
        if (taskToEdit == null) {
            System.out.println("Task not found.");
            return;
        }

        System.out.print("New title (leave empty to keep '" + taskToEdit.getTitle() + "'): ");
        String newTitle = scanner.nextLine();
        if (newTitle.isBlank()) newTitle = null;

        System.out.print("New description (leave empty to keep): ");
        String newDescription = scanner.nextLine();
        if (newDescription.isBlank()) newDescription = null;

        LocalDate newDeadline = null;
        System.out.print("New due date (yyyy-mm-dd, leave empty to keep): ");
        String dateInput = scanner.nextLine();
        if (!dateInput.isBlank()) {
            try {
                newDeadline = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date, will keep existing.");
            }
        }

        Priority newPriority = null;
        System.out.print("New priority (LOW, MEDIUM, HIGH, leave empty to keep): ");
        String priorityInput = scanner.nextLine();
        if (!priorityInput.isBlank()) {
            try {
                newPriority = Priority.valueOf(priorityInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority, will keep existing.");
            }
        }

        boolean updated = taskManager.editTask(editId, newTitle, newDescription, newDeadline, newPriority);
        if (updated) {
            System.out.println("Task updated.");
        } else {
            System.out.println("Edit failed.");
        }

    }

    private void markAsCompletedFromInput() {
        System.out.print("Enter task ID to mark as completed: ");
        try {
            UUID taskId = UUID.fromString(scanner.nextLine());
            taskManager.markTaskAsCompleted(taskId);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private void deleteTask() {
        System.out.println("Enter task ID to remove: ");

        try {
            UUID taskId = UUID.fromString(scanner.nextLine());
            boolean deleteTask = taskManager.deleteTask(taskId);

            if (deleteTask) {
                System.out.println("Task delete.");
            } else {
                System.out.println("Delete failed.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private void findTasksByTitle() {
        System.out.println("Enter task title: ");
        String taskTitle = scanner.nextLine();

        List<Task> tasks = taskManager.findTasksByTitle(taskTitle);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found with that title.");
        } else {
            tasks.forEach(task -> {
                System.out.println(task);
                System.out.println("-------------");
            });
        }
    }

    private void assignTask() {
        System.out.println("Enter task ID to assign: ");

        try {
            UUID taskId = UUID.fromString(scanner.nextLine());
            Task task =  taskManager.getTaskById(taskId);

            if (task != null) {
                System.out.print("Enter user IDs to assign to the task (comma-separated): ");
                String input = scanner.nextLine();
                String[] idUsers = input.split(",");

                for (String idUser : idUsers) {
                    try {
                        UUID id = UUID.fromString(idUser.trim());
                        User user = userManager.getUserById(id);

                        if (user != null) {
                            task.assignUser(user);
                        } else {
                            System.out.println("User with ID " + id + " not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid UUID: " + idUser.trim());
                    }

                }

            } else {
                System.out.println("Assign failed.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

}
