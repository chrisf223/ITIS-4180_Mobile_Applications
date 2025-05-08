package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    int id;
    String name;

    public ToDoList() {
    }

    public ToDoList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

