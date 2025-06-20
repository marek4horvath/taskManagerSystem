package taskmanager;

import taskmanager.service.TaskManager;
import taskmanager.service.UserManager;
import taskmanager.ui.ConsoleUI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        UserManager userManager = new UserManager();

        ConsoleUI ui = new ConsoleUI(scanner, taskManager, userManager);
        ui.run();
    }
}