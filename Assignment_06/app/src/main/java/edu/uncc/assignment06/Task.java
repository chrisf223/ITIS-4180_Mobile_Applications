package edu.uncc.assignment06;

import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private String date;
    private String priority;

    public Task(String name, String date, String priority) {
        this.name = name;
        this.date = date;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }
}

