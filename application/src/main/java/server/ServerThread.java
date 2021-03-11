package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private int clientId = -1;
    ServerThreadsManager serverThreadsManager = ServerThreadsManager.getInstance();
    DataOutputStream writer;

    public ServerThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
    }

    public void run() {
        DataInputStream reader;
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
                        return;
                    } else if (message.startsWith("/InitClient")) {
                        handleInitConnection(writer, message);
                    } else if (message.startsWith("/getClientList")) {
                        sendClientList(new ObjectOutputStream(socket.getOutputStream()));
                        System.out.println("Liste gesendet");
                    } else if (message.startsWith("/GameRequest")) {
                        sendGameRequest(message);
                        System.out.println("send game request");
                    } else if (message.startsWith("/Accept")) {
                        sendAcceptanceBack(message);
                        System.out.println("send game acceptance");
                    }else {
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

    private void sendGameRequest(String requestMessage) throws IOException {
        int firstWhiteSpace = requestMessage.indexOf(" ");
        int secondWhiteSpace = requestMessage.lastIndexOf(" ");

        int askedPlayerId = Integer.parseInt(requestMessage.substring(firstWhiteSpace, secondWhiteSpace).trim());
        int requestingPlayerId = Integer.parseInt(requestMessage.substring(secondWhiteSpace).trim());

        ServerThread askedPlayerThread = ServerThreadsManager.getInstance().getThreadByClientID(askedPlayerId);
        DataOutputStream otherWriter = askedPlayerThread.getWriter();
        String message = "/GameRequest " + requestingPlayerId;

        otherWriter.writeUTF(message);
        otherWriter.flush();
    }

    private void sendAcceptanceBack(String acceptMessage) throws IOException {
        String[] messageParts = acceptMessage.split("\\s+");
        int requestPayerId = Integer.parseInt(messageParts[1]);
        String acceptingPlayerId = messageParts[2];

        ServerThread askedPlayerThread = ServerThreadsManager.getInstance().getThreadByClientID(requestPayerId);
        DataOutputStream otherWriter = askedPlayerThread.getWriter();
        String message = "/Accepted " + acceptingPlayerId;

        otherWriter.writeUTF(message);
        otherWriter.flush();
    }

    public int getClientId(){
        return  this.clientId;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public DataOutputStream getWriter() {return this.writer;}
}
