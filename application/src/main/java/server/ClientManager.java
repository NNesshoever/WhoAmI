package server;


import Dtos.UserDto;
import services.ClientService;

import java.util.*;

public class ClientManager {
    private static List<Client> clients = new ArrayList<>();
    private static int lastId = 0;
    private Iterator<Client> i;

    public static synchronized ArrayList<UserDto> getClients(){
    ArrayList<UserDto> clientDtos = new ArrayList<>();

      for(Client client : clients){
          clientDtos.add(new UserDto(client.getId(),client.getName()));
      }
        return clientDtos;
    }

    public static synchronized Client addClient() {
        Client client = null;
        clients.add(new Client(++lastId));
        for (Client c : clients) {
            for (Client cOther : clients) {
                if (!c.getClientMessageIndex().containsKey(cOther.getId())) {
                    client = c;
                    c.getClientMessageIndex().put(cOther.getId(), 0);
                }
            }
        }
        return client;
    }

    public static synchronized Client getClient(int id) {
        Optional<Client> optClient = clients.stream().filter(x -> x.getId() == id).findFirst();
        return optClient.orElse(null);
    }

    public static synchronized void addMessage(int clientId, String message) {
        Optional<Client> optClient = clients.stream().filter(x -> x.getId() == clientId).findFirst();
        optClient.ifPresent(client -> client.getMessages().add(message));
    }

    public static synchronized String getNextMessage(int clientId) {
        for (Client c : clients) {
            if (c.getId() == clientId) continue;
            int index = c.getMessageIndex(clientId);
            if (c.getMessages().size() > index) {
                c.incrementMessageIndex(clientId);
                return "Client " + c.getId() + ":" + c.getMessages().get(index);
            }
        }
        return null;
    }

    public static synchronized void deleteClient(int clientID) {
        Client delete = ClientManager.getClient(clientID);
        if (delete != null) {
            clients.remove(delete);
        }
    }


}
