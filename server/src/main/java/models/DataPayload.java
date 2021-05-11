package models;

import java.io.Serializable;
import java.util.Arrays;

public class DataPayload implements Serializable {
    private static final long serialVersionUID = -5018457037696844853L;

    private String command;
    private String[] data;
    private Object plainData;

    public DataPayload(String command) {
        this.command = command;
    }

    public DataPayload(String command, String[] data) {
        this.command = command;
        this.data = data;
    }

    public DataPayload(String command, Object data) {
        this.command = command;
        this.plainData = data;
    }

    public String getCommand() {
        return command;
    }

    public String[] getData() {
        return data;
    }

    public Object getPlainData() {
        return plainData;
    }

    @Override
    public String toString() {
        return "DataPayload{" +
                "command='" + command + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
