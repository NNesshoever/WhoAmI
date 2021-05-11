package models;

import java.io.Serializable;

public class DataPayload implements Serializable {
    private static final long serialVersionUID = -5018457037696844853L;

    private String command;
    private String dataType;
    private Object data;

    public DataPayload(String command, Object data) {
        this.command = command;
        this.data = data;
    }

    public DataPayload(String command, Object data, String dataType) {
        this.command = command;
        this.data = data;
        this.dataType = dataType;
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
