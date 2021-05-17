package services;


import enums.Commands;
import models.DataPayload;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientService {

    private static final int PORT = 9995;
    private static Socket socket;
    private static ClientService single_instance = null;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private ClientService(String username) throws IOException, ClassNotFoundException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        objectOutputStream.writeObject(new DataPayload(Commands.SEND_LOGIN.value, new String[]{username}));
        objectOutputStream.flush();
        objectInputStream.readObject();
        single_instance = this;
    }

    public static ClientService getInstance(String username) throws IOException {
        try {
            if (single_instance == null) {
                single_instance = new ClientService(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return single_instance;
    }

    public static ClientService getInstance() {
        return single_instance;
    }

    public void sendDataPayload(DataPayload dataPayload) throws IOException {
        objectOutputStream.writeObject(dataPayload);
        objectOutputStream.flush();
        System.out.println("Send to Server "+ dataPayload);
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }
}
