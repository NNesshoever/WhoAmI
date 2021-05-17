package server;

import managers.ClientManager;
import enums.Commands;
import enums.GameStates;
import managers.GameSessionManager;
import models.Client;
import models.DataPayload;
import models.GameSession;
import models.Person;
import org.apache.log4j.Logger;
import utils.JsonService;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ServerThread extends Thread {
    protected static final Logger LOGGER = Logger.getLogger(ServerThread.class);
    private Socket socket;
    private Client client;

    private ServerThreadsManager serverThreadsManager = ServerThreadsManager.getInstance();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private DataInputStream dataInputStream;

    public ServerThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                if (dataInputStream.available() > 0) {
                    DataPayload dataPayload = (DataPayload) objectInputStream.readObject();
                    LOGGER.info("incoming payload " + dataPayload);
                    if (dataPayload == null) continue;
                    if (dataPayload.getCommand().equals(Commands.SEND_LOGOUT.value)) {
                        // TODO: Fix Logout error
                        socket.close();
                        ClientManager.deleteClient(client.getId());
                        return;
                    } else if (dataPayload.getCommand().equals(Commands.SEND_LOGIN.value)) {
                        handleInitConnection(dataPayload);
                    } else if (dataPayload.getCommand().equals(Commands.GET_CLIENT_LIST.value)) {
                        sendClientList();
                        LOGGER.info("Liste gesendet");
                    } else if (dataPayload.getCommand().equals(Commands.GET_PERSON.value)) {
                        sendPerson();
                        LOGGER.info("Person gesendet");
                    } else if (dataPayload.getCommand().equals(Commands.SEND_GAME_REQUEST.value)) {
                        forwardGameRequest(dataPayload);
                        LOGGER.info("send game request");
                    } else if (dataPayload.getCommand().equals(Commands.SEND_RESPONSE_GAME_REQUEST.value)) {
                        sendResponseGameRequest(dataPayload);
                        LOGGER.info("send response game request");
                    } else if (dataPayload.getCommand().equals(Commands.SEND_TEXT_MESSAGE.value)) {
                        sendMessage(dataPayload);
                        LOGGER.info("Send text message");
                    } else if (dataPayload.getCommand().equals(Commands.SEND_GAME_OVER.value)) {
                        informOverVictory(dataPayload);
                        LOGGER.info("informed opponent over victory");
                    } else {
                        LOGGER.info("Received message from client" + client.getId() + ": " + dataPayload.getCommand());
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

    private void sendMessage(DataPayload pDataPayload) throws IOException {
        GameSession gameSession = GameSessionManager.getGameSession(client.getId());
        DataPayload dataPayload = new DataPayload(Commands.FORWARD_MESSAGE.value, pDataPayload.getData());
        sendThreadSpecificData(GameSessionManager.getOtherId(gameSession, client.getId()), dataPayload);
    }

    private void informOverVictory(DataPayload pDataPayload) throws IOException {
        GameSession gameSession = GameSessionManager.getGameSession(client.getId());

        DataPayload dataPayload = new DataPayload(Commands.FORWARD_GAME_OVER.value, new String[]{pDataPayload.getData()[0], client.getPerson().getFullName()});
        sendThreadSpecificData(GameSessionManager.getOtherId(gameSession, client.getId()),dataPayload);
    }

    private void handleInitConnection(DataPayload pDataPayload)
            throws IOException {
        String clientName = pDataPayload.getData()[0];
        client = ClientManager.addClient(clientName);

        DataPayload dataPayload = new DataPayload(Commands.ANSWER_INIT_CLIENT.value);
        sendDataPayload(dataPayload);
    }


    private void sendClientList() {
        DataPayload dataPayload = new DataPayload(Commands.ANSWER_CLIENT_LIST.value,
                ClientManager.getClients(client.getId()));
        sendDataPayload(dataPayload);
    }

    private void sendPerson() {
        DataPayload dataPayload = new DataPayload(Commands.ANSWER_PERSON.value, client.getPerson());
        sendDataPayload(dataPayload);
    }


    private void forwardGameRequest(DataPayload pDataPayload) throws IOException {
        int opponentId = Integer.parseInt(pDataPayload.getData()[0]);
        GameSession gameSession = GameSessionManager.openGameSession(client, ClientManager.getClient(opponentId));

        DataPayload dataPayload = new DataPayload(Commands.FORWARD_GAME_REQUEST.value, new String[]{
                Integer.toString(gameSession.getClient().getId()), gameSession.getClient().getName()});

        sendThreadSpecificData(opponentId, dataPayload);
    }

    private void sendResponseGameRequest(DataPayload pDataPayload) throws IOException {
        boolean isAccepted = Boolean.parseBoolean(pDataPayload.getPlainData().toString());
        GameSession gameSession = GameSessionManager.getGameSession(client.getId());

        if (isAccepted) {
            GameSessionManager.updateGameSessionState(client.getId(), GameStates.STARTED);
        } else {
            GameSessionManager.closeGameSession(client.getId());
        }

        DataPayload dataPayload = new DataPayload(Commands.FORWARD_RESPONSE_GAME_REQUEST.value,
                pDataPayload.getPlainData());
        sendThreadSpecificData(GameSessionManager.getOtherId(gameSession, client.getId()), dataPayload);
    }

    public void sendThreadSpecificData(int id, DataPayload pDataPayload) throws IOException {
        ServerThread thread = ServerThreadsManager.getInstance().getThreadByClientID(id);
        ObjectOutputStream writer = thread.getWriter();
        writer.writeObject(pDataPayload);
        LOGGER.info("Other Datapayload " + thread.client.getId() + " " + thread.client.getName() + " " + pDataPayload);

    }

    private void sendDataPayload(DataPayload dataPayload) {
        try {
            objectOutputStream.writeObject(dataPayload);
            LOGGER.info("Own Datapayload " + client.getId() + " " + client.getName() + " " + dataPayload);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return client.getId();
    }

    public ObjectOutputStream getWriter() {
        return this.objectOutputStream;
    }
}
