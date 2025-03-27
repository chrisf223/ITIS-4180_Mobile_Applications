package edu.uncc.assessment03.models;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int age;
    private int creditScore;
    private State state;

    public User() {
    }

    public User(String name, int age, int creditScore, State state) {
        this.name = name;
        this.age = age;
        this.creditScore = creditScore;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
