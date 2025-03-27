package edu.uncc.assignment09.models;

public class CreditCategory {
    String name;
    int imageResourceId;

    public CreditCategory() {
    }

    public CreditCategory(String name, int imageResource) {
        this.name = name;
        this.imageResourceId = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
