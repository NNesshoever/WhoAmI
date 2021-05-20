package models;

import enums.GameStates;

public class GameSession {
    private Client client;
    private Person clientPerson;

    private Client opponentClient;
    private Person opponentPerson;

    private GameStates currentGameState;

    public GameSession(Client client, Client opponentClient, GameStates currentGameState) {
        this.client = client;
        this.opponentClient = opponentClient;
        this.currentGameState = currentGameState;
    }

    public Client getClient() {
        return client;
    }

    public Client getOpponentClient() {
        return opponentClient;
    }

    public Person getClientPerson() {
        return clientPerson;
    }

    public Person getOpponentPerson() {
        return opponentPerson;
    }

    public void setClientPerson(Person clientPerson) {
        this.clientPerson = clientPerson;
    }

    public void setOpponentPerson(Person opponentPerson) {
        this.opponentPerson = opponentPerson;
    }

    public void setOpponentClient(Client opponentClient) {
        this.opponentClient = opponentClient;
    }

    public GameStates getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameStates currentGameState) {
        this.currentGameState = currentGameState;
    }
}
