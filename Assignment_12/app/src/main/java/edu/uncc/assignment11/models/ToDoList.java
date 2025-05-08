package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    String name;
    String createdBy;
    String toDoListID;

    public ToDoList() {
    }

    public ToDoList(String name, String createdBy, String toDoListID) {
        this.name = name;
        this.createdBy = createdBy;
        this.toDoListID = toDoListID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void createdBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getToDoListID() {
        return toDoListID;
    }

    public void setToDoListID(String toDoListID) {
        this.toDoListID = toDoListID;
    }
}
