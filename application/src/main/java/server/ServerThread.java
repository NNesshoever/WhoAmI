package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private int clientId = -1;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        DataInputStream reader;
        DataOutputStream writer;
        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                if (clientId > 0) {
                    handleMessages(writer);
                }
                if (reader.available() > 0) {
                    String message = reader.readUTF();
                    if (message.isEmpty()) continue;

                    if (message.startsWith("/Quit")) {
                        socket.close();
                        ClientManager.deleteClient(clientId);
                        return;
                    } else if (message.startsWith("/InitClient")) {
                        handleInitConnection(writer, message);
                    } else if (message.startsWith("/getClientList")) {
                        sendClientList(new ObjectOutputStream(socket.getOutputStream()));
                        System.out.println("Liste gesendet");
                    } else {
                        System.out.println("Received message from client" + clientId + ": " + message);
                        ClientManager.addMessage(clientId, message);
                    }
                }


            } catch (EOFException ignored) {

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {

            }
        }
    }

    private void handleMessages(DataOutputStream writer) throws IOException {
        String message;
        while ((message = ClientManager.getNextMessage(clientId)) != null) {
            writer.writeUTF(message);
            writer.flush();
        }
    }

    private void handleInitConnection(DataOutputStream writer, String initMessage) throws IOException {
        Client client = ClientManager.addClient();
        client.setName(getNameFromInit(initMessage));
        clientId = client.getId();
        String message = String.valueOf(clientId);
        System.out.println("Client connected with Id:" + message);
        writer.writeUTF(message);
        writer.flush();
    }

    private String getNameFromInit(String init) {
        return init.split(",")[1];
    }

    private void sendClientList(ObjectOutputStream writer){
        try {
            writer.writeObject(ClientManager.getClients());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
