package models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -7594733687016500538L;
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.id + ")";
    }
}
