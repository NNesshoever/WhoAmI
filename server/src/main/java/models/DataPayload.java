package models;

import java.io.Serializable;

public class DataPayload implements Serializable {

    private String command;
    private String dataType;
    private Object data;

    public DataPayload(String command, Object data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public String getDataType() {
        return dataType;
    }

    public Object getData() {
        return data;
    }

}
