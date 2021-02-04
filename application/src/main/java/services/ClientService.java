package services;


import Dtos.UserDto;

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

    private static ClientService instance;
    private static ClientService single_instance = null;

    private ClientService(String username) throws IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        dos = new DataOutputStream(socket.getOutputStream());
        this.name = username;
        dos.writeUTF("/InitClient,"+ username);
        dos.flush();
        dis = new DataInputStream(socket.getInputStream());
        String id = dis.readUTF();
        clientId = Integer.parseInt(id);
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
            while (true) {
                try {
                    if (dis.available() > 0) {
                        System.out.println(dis.readUTF());
                    }
                } catch (IOException ignored) {

                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {

                }
            }

        });
        t.start();
    }

    public static ClientService getInstance(String username) throws IOException {
        if (single_instance == null) {
            single_instance = new ClientService(username);
        }
        return single_instance;
    }

    public ArrayList<UserDto> getClientList(){
        ArrayList<UserDto> userDtos = null;
        try {
            dos.writeUTF("/getClientList");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            //TODO: make Serializable
            userDtos = (ArrayList<UserDto>) disObject.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userDtos;
    }

    public int getClientId(){
        return this.clientId;
    }
}
