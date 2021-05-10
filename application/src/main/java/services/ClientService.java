package services;


import Dtos.PersonDto;
import Dtos.UserDto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClientService {

    private static final int PORT = 9995;
    private static Socket socket;
    private static String latestMessage;
    private static String latestTextMessage;
    private static ClientService instance;
    private static ClientService single_instance = null;
    private int clientId = -1;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectInputStream disObject;
    private boolean ContinueRead = true;
    private boolean ReadIsRunning;

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
        latestTextMessage = null;
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

    public static void setInstance() {
        single_instance = null;
    }

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
                            if (latestMessage.startsWith("/Accept") || latestMessage.startsWith("/recMessage") || latestMessage.startsWith("/opponentLost")) {
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

    public ArrayList<UserDto> getClientList() {
        ArrayList<UserDto> userDtos = null;
        try {
            ContinueRead = false;
            dos.writeUTF("/getClientList");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            userDtos = (ArrayList<UserDto>) disObject.readObject();
            ContinueRead = true;
            read();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userDtos;
    }

    public PersonDto getPerson() {
        PersonDto Person = null;
        try {
            ContinueRead = false;
            dos.writeUTF("/GetPerson");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            Person = (PersonDto) disObject.readObject();
            ContinueRead = true;
            read();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Person;
    }

    public int getClientId() {
        return this.clientId;
    }

}
