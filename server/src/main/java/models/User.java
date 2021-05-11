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

    @Override
    public String toString() {
        return this.name + " (" + this.id + ")";
    }
}
