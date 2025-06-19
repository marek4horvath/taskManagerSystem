package main.java.taskmanager.service;

import main.java.taskmanager.model.Task;
import main.java.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private final List<User> users = new ArrayList<User>();

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
