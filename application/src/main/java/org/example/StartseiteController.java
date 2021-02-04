package org.example;

import Dtos.UserDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import services.ClientService;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.TextField;

public class StartseiteController {
    ObservableList<UserDto> usersList;

    private String username = "";
    ClientService client;

    @FXML
    ListView<UserDto> PlayersList;

    @FXML
    public void initialize() {
        usersList = FXCollections.observableArrayList();
        PlayersList.setItems(usersList);
        loadClientList();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void loadClientList() {
        try {
            client = ClientService.getInstance(this.username);
            ArrayList<UserDto> users = client.getClientList();
            usersList.setAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Button LogoutButton;
    @FXML
    TextField usernameTextField;

    private ClientService _clientService;

    public StartseiteController() {
        //TODO: usernameTextField gibt es nicht
//        try {
//            String textValue = usernameTextField.getText();
//            _clientService = ClientService.getInstance(textValue);
//        }
//        catch (IOException e){//TODO: Open error Toast
//        }
    }

    @FXML
    public void RefreshPlayers() {
        loadClientList();
    }

    public void SendGameRequest() {
        String message = "/GameRequest " + _clientService.getClientId();
        try {
            _clientService.sendText(message);
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }

    }

    public void StartGame() {

    }

    @FXML
    public void LogoutUser(){

    }


}
