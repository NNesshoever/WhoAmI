package dtos;

import java.io.Serializable;

public class UserDto implements Serializable {
    private int id;
    private String name;

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public UserDto(){}

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
