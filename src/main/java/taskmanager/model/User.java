package main.java.taskmanager.model;

import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String surname;
    private String password;

    public User(String name, String surname, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User: \n ID: %s\n Name: %s\n Surname: %s\n",
            id,
            name,
            surname
        );
    }
}
