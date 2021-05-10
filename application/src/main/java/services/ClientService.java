package services;


import enums.Commands;
import models.DataPayload;
import models.Person;
import models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClientService {

    private static final int PORT = 9995;
    private static Socket socket;
    private static DataPayload dataPayload;
    private static String latestTextMessage;
    private static ClientService instance;
    private static ClientService single_instance = null;
    private int clientId = -1;
        private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private boolean ContinueRead = true;


    private ClientService(String username) throws IOException, ClassNotFoundException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(new DataPayload(Commands.INIT_CLIENT.value, username));
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        DataPayload dataPayload = (DataPayload) objectInputStream.readObject();
        clientId = Integer.parseInt(dataPayload.getData().toString());
        single_instance = this;
    }

    public static ClientService getInstance(String username) throws IOException {
        try {
            if (single_instance == null) {
                single_instance = new ClientService(username);
                single_instance.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return single_instance;
    }

    public static String getLatestTextMessage() {
        String text = latestTextMessage;
        latestTextMessage = "";
        return text;
    }

    public static void setLatestTextMessage(String latestTextMessage) {
        ClientService.latestTextMessage = latestTextMessage;
    }

    public static void setPayload(DataPayload dataPayload) {
        ClientService.dataPayload = dataPayload;
    }

    public static void setInstance() {
        single_instance = null;
    }


    public void sendText(String message) throws IOException {
        if (clientId > 0) {
            objectOutputStream.writeUTF(message);
            objectOutputStream.flush();
        }
    }

    public void read() {
        Thread t = new Thread(() -> {
            while (ContinueRead) {
                try {
                    if (objectInputStream.available() > 0) {
                        dataPayload = (DataPayload) objectInputStream.readObject();
                        System.out.println(dataPayload);
                        if (dataPayload.getCommand().startsWith(Commands.SEND_OPPONENT_LOST.value)) {
                            latestTextMessage = dataPayload.getData().toString();
                        } else {
                            int messageKey = dataPayload.getData().toString().split(" ")[0].length() + 1;
                            latestTextMessage = dataPayload.getData().toString().substring(messageKey);
                        }

                    }
                } catch (IOException | ClassNotFoundException ignored) {
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
    }

    public ArrayList<User> getClientList() {
        ArrayList<User> users = null;
        try {
            ContinueRead = false;
            objectOutputStream.writeObject(new DataPayload(Commands.GET_CLIENT_LIST.value, null));
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            users = (ArrayList<User>) objectInputStream.readObject();
            ContinueRead = true;
            read();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Person getPerson() {
        Person Person = null;
        try {
            ContinueRead = false;
            objectOutputStream.writeObject(new DataPayload(Commands.GET_PERSON.value, null));
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Person = (models.Person) objectInputStream.readObject();
            ContinueRead = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Person;
    }

    public int getClientId() {
        return this.clientId;
    }
    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }
}
