package managers;


import models.Client;
import models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ClientManager {
    private static List<Client> clients = new ArrayList<>();
    private static int clientCount =0;

    public static synchronized ArrayList<User> getClients(int clientId) {
        ArrayList<User> clientList = new ArrayList<>();
        for (Client client : clients) {
            if(client.getId() != clientId){
                clientList.add(new User(client.getId(), client.getName()));
            }
        }
        return clientList;
    }

    public static synchronized Client addClient(String name) {
        Client client = null;
        clients.add(new Client(clientCount++, name));
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
