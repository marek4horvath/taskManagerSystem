package taskmanager.ui;

import taskmanager.model.Task;
import taskmanager.model.Priority;
import taskmanager.service.TaskManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {

    private final Scanner scanner;
    private final TaskManager taskManager;

    public ConsoleUI(Scanner scanner, TaskManager taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    private String menu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Add task");
        System.out.println("2. Show all tasks");
        System.out.println("3. Mark task as completed");
        System.out.println("4. Edit task");
        System.out.println("5. Delete task");
        System.out.println("6. Find tasks by title");
        System.out.println("0. Exit");
        System.out.print("Entry option: ");

        return scanner.nextLine();
    }

    public void run() {
        while (true) {
            String choice = menu();

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
                    System.out.print("Enter task ID to mark as completed: ");
                    try {
                        UUID taskId = UUID.fromString(scanner.nextLine());
                        taskManager.markTaskAsCompleted(taskId);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid UUID format.");
                    }
                    break;
                case "4":
                    editTaskFromInput();
                    break;
                case "5":
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

                    break;
                case "6":
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

                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private Task createTaskFromInput() {
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

        return new Task(title, description, dueDate, priority);
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
}
