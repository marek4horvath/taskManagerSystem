package taskmanager;

import taskmanager.service.TaskManager;
import taskmanager.ui.ConsoleUI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        ConsoleUI ui = new ConsoleUI(scanner, taskManager);
        ui.run();
    }
}