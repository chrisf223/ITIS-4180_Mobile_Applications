package com.example.evaluation_2;

import java.io.Serializable;

public class Calorie implements Serializable {
    private String Gender;
    private String Weight;
    private String Height;
    private String Age;
    private String ActivityLevel;

    // Constructor
    public Calorie(String Gender, String Weight, String Height, String Age, String ActivityLevel) {
        this.Gender = Gender;
        this.Weight = Weight;
        this.Height = Height;
        this.Age = Age;
        this.ActivityLevel = ActivityLevel;
    }

    // Getters and Setters

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String Height) {
        this.Height = Height;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getActivityLevel() {
        return ActivityLevel;
    }

    public void setActivityLevel(String ActivityLevel) {
        this.ActivityLevel = ActivityLevel;
    }
}

