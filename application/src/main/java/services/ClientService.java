package services;


import enums.Commands;
import models.Person;
import models.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClientService {

    private static Socket socket;
    private static final int PORT = 9995;
    private int clientId = -1;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectInputStream disObject;
    private static String latestMessage;
    private static String latestTextMessage;
    private boolean ContinueRead = true;
    private boolean ReadIsRunning;
    private static ClientService instance;
    private static ClientService single_instance = null;


    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public ObjectInputStream getDisObject() {
        return disObject;
    }

    public void setDisObject(ObjectInputStream disObject) {
        this.disObject = disObject;
    }

    private ClientService(String username) throws IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("/InitClient," + username);
        dos.flush();
        dis = new DataInputStream(socket.getInputStream());
        String id = dis.readUTF();
        clientId = Integer.parseInt(id);
        single_instance = this;
    }

    /**
     * Singleton Pattern
     *
     * @param username
     * @return
     * @throws IOException
     */
    public static ClientService getInstance(String username) throws IOException {
        if (single_instance == null) {
            single_instance = new ClientService(username);
            single_instance.read();
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

    public static String getLatestMessage() {
        return latestMessage;
    }

    public static void setLatestMessage(String latestMessage) {
        ClientService.latestMessage = latestMessage;
    }

    /**
     * Sends a text message to the server and outputs the response message.
     *
     * @param message The message to send to the server.
     * @throws IOException When stream usage fails.
     */
    public void sendText(String message) throws IOException {
        if (clientId > 0) {
            dos.writeUTF(message);
            dos.flush();
        }
    }

    public void read() {
        Thread t = new Thread(() -> {
            if (!ReadIsRunning) {
                while (ContinueRead) {
                    ReadIsRunning = true;
                    try {
                        if (dis.available() > 0) {
                            latestMessage = dis.readUTF();
                            System.out.println(latestMessage);
                            if (latestMessage.startsWith("/Accept") || latestMessage.startsWith(Commands.REPLY_SEND_GAME_REQUEST.value) || latestMessage.startsWith("/recMessage") || latestMessage.startsWith("/opponentLost")) {
                                System.out.println(latestMessage);
                                if (latestMessage.startsWith("/opponentLost")) {
                                    latestTextMessage = latestMessage;
                                } else {
                                    int messageKey = latestMessage.split(" ")[0].length() + 1;
                                    latestTextMessage = latestMessage.substring(messageKey);
                                }
                            }
                        }
                    } catch (IOException ignored) {

                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {

                    }
                }
            }
            ReadIsRunning = false;
        });
        t.start();
    }

    public static void setInstance() {
        single_instance = null;
    }

    public ArrayList<User> getClientList() {
        ArrayList<User> users = null;
        try {
            ContinueRead = false;
            dos.writeUTF("/getClientList");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            users = (ArrayList<User>) disObject.readObject();
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
            dos.writeUTF("/GetPerson");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            Person = (models.Person) disObject.readObject();
            ContinueRead = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Person;
    }

    public int getClientId() {
        return this.clientId;
    }

}
