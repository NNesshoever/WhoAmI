package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.example.Dtos.UserDto;
import services.ClientService;

import java.io.IOException;
import java.util.ArrayList;

public class StartseiteController {
    ObservableList<UserDto> usersList;

    private String username = "";
    ClientService client;

    @FXML
    ListView<UserDto> PlayersList;

    @FXML
    public void initialize(){
        usersList = FXCollections.observableArrayList();
        PlayersList.setItems(usersList);
        loadClientList();
    }

    public void setUsername(String username){
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

    @FXML
    public void RefreshPlayers() {
        loadClientList();
    }

    @FXML
    public void LogoutUser(){

    }
}
