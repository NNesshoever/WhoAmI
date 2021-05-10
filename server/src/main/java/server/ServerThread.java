package server;

import client.ClientManager;
import enums.Commands;
import models.Client;
import models.DataPayload;
import models.Person;
import utils.JsonService;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ServerThread extends Thread {
    private Socket socket;
    private int clientId = -1;
    private ServerThreadsManager serverThreadsManager = ServerThreadsManager.getInstance();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ServerThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
    }

    public void run() {
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                if (clientId > 0) {
                }
                if (dataInputStream.available() > 0) {
                    DataPayload dataPayload = (DataPayload) objectInputStream.readObject();
                    if (dataPayload == null) continue;
                    if (dataPayload.getCommand().startsWith(Commands.QUIT.action)) {
                        socket.close();
                        ClientManager.deleteClient(clientId);
                        return;
                    } else if (dataPayload.getCommand().startsWith(Commands.INIT_CLIENT.action)) {
                        handleInitConnection(objectOutputStream, dataPayload);
                    } else if (dataPayload.getCommand().startsWith(Commands.GET_CLIENT_LIST.action)) {
                        sendClientList(new ObjectOutputStream(socket.getOutputStream()));
                        System.out.println("Liste gesendet");
                    } else if (dataPayload.getCommand().startsWith(Commands.GET_PERSON.action)) {
                        Person person = getRandomPerson();
                        sendPerson(new ObjectOutputStream(socket.getOutputStream()), person);
                    } else if (dataPayload.getCommand().startsWith(Commands.SEND_GAME_REQUEST.action)) {
                        sendGameRequest(dataPayload.getCommand());
                        System.out.println("send game request");
                    } else if (dataPayload.getCommand().startsWith(Commands.ACCEPT_GAME_REQUEST.action)) {
                        sendAcceptanceBack(dataPayload.getCommand());
                        System.out.println("send game acceptance");
                    } else if (dataPayload.getCommand().startsWith(Commands.SEND_TEXT_MESSAGE.action)) {
                        sendMessage(dataPayload.getCommand());
                        System.out.println("Send Text Message");
                    } else if (dataPayload.getCommand().startsWith(Commands.SEND_OPPONENT_LOST.action)) {
                        informOverVictory(dataPayload.getCommand());
                        System.out.println("informed opponent over victory");
                    } else {
                        System.out.println("Received message from client" + clientId + ": " + dataPayload.getCommand());
                        ClientManager.addMessage(clientId, dataPayload.getCommand());
                    }
                }

            } catch (EOFException ignored) {

            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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

    private void sendMessage(String textmessage) throws IOException {
        String text = textmessage.split(",")[1];
        int recUserId = Integer.parseInt(textmessage.split(",")[2]);

        ServerThread recUserThread = ServerThreadsManager.getInstance().getThreadByClientID(recUserId);
        ObjectOutputStream otherWriter = recUserThread.getWriter();
        String message = Commands.RECEIVE_MESSAGE.action + " " + text;

        otherWriter.writeUTF(message);
        otherWriter.flush();

    }

    private void informOverVictory(String message) throws IOException {
        String[] messageParts = message.split("\\s+");
        int winningPlayerId = Integer.parseInt(messageParts[1]);
        String opponentId = messageParts[2];

        ServerThread askedPlayerThread = ServerThreadsManager.getInstance().getThreadByClientID(winningPlayerId);
        ObjectOutputStream otherWriter = askedPlayerThread.getWriter();
        String messagetoSend = Commands.SEND_OPPONENT_LOST.action + " " + winningPlayerId + " " + opponentId;

        otherWriter.writeUTF(messagetoSend);
        otherWriter.flush();
    }

    private void handleInitConnection(ObjectOutputStream objectOutputStream, DataPayload dataPayload) throws IOException {
        Client client = ClientManager.addClient();
        client.setName(dataPayload.getData().toString());
        clientId = client.getId();
        System.out.println("Client connected with Id:" + clientId);
        objectOutputStream.writeObject(new DataPayload(Commands.REPLY_INIT_CLIENT.action, clientId));
        objectOutputStream.flush();
    }


    private Person getRandomPerson() {
        List<Person> Persons = JsonService.loadJson();
        Random rnd = new Random();
        int max = Persons.size();
        long seed = System.nanoTime();
        seed ^= (seed << rnd.nextInt());
        seed ^= (seed >> rnd.nextInt());
        String SeedString = String.valueOf(seed);
        int i = 0;
        int temp = 0;
        do {
            temp = Character.digit(SeedString.charAt(i), 10);
            ++i;

        } while (temp > max - 1 && temp > 0 && i <= SeedString.length());
        Person returnPerson = new Person();
        returnPerson = Persons.get(temp);
        return returnPerson;
    }

    private String getNameFromInit(String init) {
        return init.split(",")[1];
    }

    private void sendClientList(ObjectOutputStream writer) {
        try {
            writer.writeObject(ClientManager.getClients());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPerson(ObjectOutputStream writer, Person person) {
        try {
            writer.writeObject(person);
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
        ObjectOutputStream otherWriter = askedPlayerThread.getWriter();
        String message = Commands.SEND_GAME_REQUEST.action + " " + requestingPlayerId;

        otherWriter.writeUTF(message);
        otherWriter.flush();
    }

    private void sendAcceptanceBack(String acceptMessage) throws IOException {
        String[] messageParts = acceptMessage.split("\\s+");
        int requestPayerId = Integer.parseInt(messageParts[1]);
        String acceptingPlayerId = messageParts[2];

        ServerThread askedPlayerThread = ServerThreadsManager.getInstance().getThreadByClientID(requestPayerId);
        ObjectOutputStream otherWriter = askedPlayerThread.getWriter();
        String message = Commands.REPLY_SEND_GAME_REQUEST.action + " " + acceptingPlayerId;

        otherWriter.writeUTF(message);
        otherWriter.flush();
    }

    public int getClientId() {
        return this.clientId;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public ObjectOutputStream getWriter() {
        return this.objectOutputStream;
    }
}
