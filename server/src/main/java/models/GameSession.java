package models;

import enums.GameStates;

public class GameSession {
    private Client client;
    private Client opponentClient;

    private GameStates currentGameState = GameStates.NOT_INITIALIZED;

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
