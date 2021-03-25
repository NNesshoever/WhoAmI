package services;


import Dtos.PersonDto;
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
    private static String latestMessage;
    private static String latestTextMessage;
    private boolean ContinueRead = true;
    private boolean ReadIsRunning;
    private static ClientService instance;
    private static ClientService single_instance = null;

    private ClientService(String username) throws IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("/InitClient,"+ username);
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

    public static String getLatestMessage(){
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
            if(!ReadIsRunning) {
                while (ContinueRead) {
                    ReadIsRunning = true;
                    try {
                        if (dis.available() > 0) {
                            latestMessage = dis.readUTF();
                            if(latestMessage.startsWith("/recMessage")){
                                int messageKey = latestMessage.split(" ")[0].length()+1;
                                latestTextMessage = latestMessage.substring(messageKey);
                                System.out.println(latestTextMessage);
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

    public static void setInstance(){
        single_instance = null;
    }

    public ArrayList<UserDto> getClientList(){
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

    public PersonDto getPerson(){
        PersonDto Person = null;
        try{
            ContinueRead = false;
            dos.writeUTF("/GetPerson");
            dos.flush();
            disObject = new ObjectInputStream(socket.getInputStream());
            Person = (PersonDto) disObject.readObject();
            ContinueRead = true;
            read();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return Person;
    }

    public int getClientId(){
        return this.clientId;
    }

}
