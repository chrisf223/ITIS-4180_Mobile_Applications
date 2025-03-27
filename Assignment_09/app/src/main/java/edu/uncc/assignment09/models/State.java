package edu.uncc.assignment09.models;

import java.io.Serializable;

public class State implements Serializable {
    String name;
    String abbreviation;
    public State() {
    }

    public State(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getStateSummary(){
        return name + ", " + abbreviation;
    }

    @Override
    public String toString() {
        return name + " (" + abbreviation + ")";
    }
}
