package edu.uncc.assignment08.models;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    public String name;
    public Date date;
    public int priority;

    public Task() {
    }

    public Task(String name, Date date, int priority) {
        this.name = name;
        this.date = date;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
