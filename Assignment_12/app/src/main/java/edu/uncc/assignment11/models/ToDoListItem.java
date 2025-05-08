package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoListItem implements Serializable {
    String name;
    String priority;
    String parentID;
    String itemID;

    public ToDoListItem() {
    }

    public ToDoListItem(String name, String priority, String parentID, String itemID) {
        this.name = name;
        this.priority = priority;
        this.itemID = itemID;
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
