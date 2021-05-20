package managers;

import enums.GameStates;
import models.Client;
import models.GameSession;
import utils.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameSessionManager {

    private static List<GameSession> sessions = new ArrayList<>();

    public static synchronized GameSession openGameSession(Client client, Client opponentClient) {
        client.setPerson(PersonService.getRandomPerson());
        opponentClient.setPerson(PersonService.getRandomPerson());
        GameSession gameSession = new GameSession(client, opponentClient, GameStates.REQUESTED);
        sessions.add(gameSession);
        return gameSession;
    }

    public static synchronized GameSession getGameSession(int clientId) {
        Optional<GameSession> optionalGameSession = sessions.stream().filter(
                session -> session.getClient().getId() == clientId
                        || session.getOpponentClient().getId() == clientId).findFirst();
        return optionalGameSession.orElse(null);
    }

    public static synchronized void closeGameSession(int clientID) {
        GameSession gameSession = GameSessionManager.getGameSession(clientID);
        if (gameSession != null) {
            sessions.remove(gameSession);
        }
    }

    public static synchronized void updateGameSessionState(int clientId, GameStates state) {
        GameSession gameSession = GameSessionManager.getGameSession(clientId);
        int index = sessions.indexOf(gameSession);
        gameSession.setCurrentGameState(state);
        sessions.set(index, gameSession);
    }

    public static synchronized int getOtherId(GameSession gameSession, int clientId) {
        if (clientId == gameSession.getClient().getId()) {
            return gameSession.getOpponentClient().getId();
        } else {
            return gameSession.getClient().getId();
        }
    }
}
