package ir.dotin.server.business;

import java.io.Serializable;

public class Deposit implements Serializable {

    private String name;
    private int id;
    private int initialBalance;
    private int upperBound;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitalBalance() {
        return initialBalance;
    }

    public void setInitalBalance(int initalBalance) {
        this.initialBalance = initalBalance;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
}
