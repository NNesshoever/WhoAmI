package org.example;

import Dtos.UserDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import server.ClientManager;
import services.ClientService;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.TextField;

public class StartseiteController {
    ObservableList<UserDto> usersList;
    Button LogoutButton;

    @FXML
    TextField usernameTextField;
    TextField shownMessage;
    Button accept;
    Button decline;

    private ClientService _clientService;

    private String username = "";
    ClientService client;
    private String activeMessage;
    private String messageToShow;

    @FXML
    ListView<UserDto> PlayersList;




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
    public void initialize(){
        usersList = FXCollections.observableArrayList();
        PlayersList.setItems(usersList);
        loadClientList();
        setActiveMessage();
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

    @FXML
    public void RefreshPlayers() {
        loadClientList();
    }

    public void SendGameRequest() throws IOException {
        int selcetedID = PlayersList.getSelectionModel().getSelectedItem().getId();
        int clientID = ClientService.getInstance(this.username).getClientId();
        String message = "/GameRequest " + PlayersList.getSelectionModel().getSelectedItem().getId() + " " + _clientService.getInstance(this.username).getClientId();
        try {
            _clientService.getInstance(this.username).sendText(message);
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public void setActiveMessage(){
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    if(activeMessage == null){
                        activeMessage = _clientService.getInstance(this.username).getLatestMessage();

                        if(activeMessage.startsWith("/GameRequest")){
                            displayRequest();
                        }
                        else if(activeMessage.startsWith("/Accept")){
                            OpenGameView();
                        }
                    }


                } catch (Exception e) {
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {

                }
            }

        });
        t.start();
    }

    public void displayRequest(){
        String message = prepareMessage();
        shownMessage.setText(message);
    }

    public String prepareMessage(){
        String actualMessage = activeMessage.substring(0, activeMessage.lastIndexOf(" "));
        String playerId = activeMessage.substring(activeMessage.lastIndexOf(" "));

        switch (actualMessage){
            case "/GameRequest":
                messageToShow = "You got a game request from " + playerId + ". Do you accept?";
                break;
            default:
                activeMessage = null;
        }

        return messageToShow;
    }

    public void AcceptGame() {
        String otherPlayerId = splitMessage();
        String message = "/Accept " + otherPlayerId + " " + _clientService.getClientId();
        try {
            _clientService.getInstance(this.username).sendText(message);
            OpenGameView();
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public void DeclineRequest(){

        String otherPlayerId = splitMessage();
        String message = "/Decline " + otherPlayerId + " " + _clientService.getClientId();
        try {
            _clientService.getInstance(this.username).sendText(message);
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public String splitMessage(){
        String[] messageParts = messageToShow.split("\\\\s+");
        return messageParts[6].substring(0, messageParts.length - 1);
    }

    public void OpenGameView() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("game.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    public void LogoutUser(){
        ClientManager.deleteClient(client.getClientId());
        try {
            Stage stage = (Stage) PlayersList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            LoginController controller = loader.getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
