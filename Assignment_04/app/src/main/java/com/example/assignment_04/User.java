package com.example.assignment_04;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String role;

    // Constructor
    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

