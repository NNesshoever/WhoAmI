package services;


import enums.Commands;
import models.DataPayload;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientService {

    private static int port = 9995;
    private static String serverUrl = "";
    private static Socket socket;
    private static ClientService single_instance = null;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private ClientService(String username) throws IOException, ClassNotFoundException {
        if (serverUrl.isEmpty()) {
            serverUrl = InetAddress.getLocalHost().getHostName();
        }
        socket = new Socket(serverUrl, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        objectOutputStream.writeObject(new DataPayload(Commands.SEND_LOGIN.value, new String[]{username}));
        objectOutputStream.flush();
        objectInputStream.readObject();
        single_instance = this;
    }

    public static ClientService getInstance(String username) throws IOException, ClassNotFoundException {
        if (single_instance == null) {
            single_instance = new ClientService(username);
        }
        return single_instance;
    }

    public static ClientService getInstance() {
        return single_instance;
    }

    public void sendDataPayload(DataPayload dataPayload) throws IOException {
        objectOutputStream.writeObject(dataPayload);
        objectOutputStream.flush();
        System.out.println("Send to Server " + dataPayload);
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void logout() {
        try {
            DataPayload dataPayload = new DataPayload(Commands.SEND_LOGOUT.value);
            getInstance().sendDataPayload(dataPayload);
        } catch (Exception ignored) {
        }
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        single_instance = null;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public static void setHost(String serverUrl) {
        serverUrl = serverUrl;
    }

    public static void setPort(int port) {
        port = port;
    }

    public static int getPort() {
        return port;
    }
}
