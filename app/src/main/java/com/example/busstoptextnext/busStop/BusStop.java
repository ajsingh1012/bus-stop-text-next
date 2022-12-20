package com.example.busstoptextnext.busStop;

import java.io.Serializable;

public class BusStop implements Serializable {
    private String description, number;

    // required empty constructor
    public BusStop() {}

    public BusStop(String description, String number) {
        this.description = description;
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
