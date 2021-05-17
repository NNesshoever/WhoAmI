package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private int id;
    private String name;
    private Map<Integer, Integer> clientMessageIndex = new HashMap<>();
    private List<String> messages = new ArrayList<>();
    private Person person;

    public Client(int id) {
        this.id = id;
    }

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(){}

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public int getMessageIndex(int clientId) {
        return clientMessageIndex.get(clientId);
    }

    public void incrementMessageIndex(int clientId) {
        clientMessageIndex.put(clientId, clientMessageIndex.get(clientId) + 1);
    }

    public Map<Integer, Integer> getClientMessageIndex() {
        return clientMessageIndex;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
