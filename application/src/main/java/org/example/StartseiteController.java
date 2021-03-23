package org.example;

import Dtos.UserDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import services.ClientService;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.TextField;

public class StartseiteController {
    ObservableList<UserDto> usersList;
    Button LogoutButton;

    @FXML
    TextField ShownMessage;

    private ClientService _clientService;

    private String username = "";
    private int opponentId;
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
            users.removeIf((clientDto)-> clientDto.getId() == client.getClientId());
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
        opponentId = PlayersList.getSelectionModel().getSelectedItem().getId();
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
                            opponentId = Integer.parseInt(activeMessage.split(" ")[1]);
                            displayRequest();
                        }
                        else if(activeMessage.startsWith("/Accepted")){
                            opponentId = Integer.parseInt(activeMessage.split(" ")[1]);
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
        ShownMessage.setText(message);
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

    public void AcceptRequest() {
        String otherPlayerId = splitMessage();
        try {
            String message = "/Accept " + otherPlayerId + " " + _clientService.getInstance(username).getClientId();
            _clientService.getInstance(this.username).sendText(message);
            OpenGameView();
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public void DeclineRequest(){

        String otherPlayerId = splitMessage();
        try {
            String message = "/Decline " + otherPlayerId + " " + _clientService.getInstance(username).getClientId();
            _clientService.getInstance(this.username).sendText(message);
            activeMessage = null;
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public String splitMessage(){
        String[] messageParts = messageToShow.split("\\s+");
        return messageParts[6].substring(0, messageParts[6].length()-1);
    }

    public synchronized void OpenGameView() throws IOException {
        Stage stage = (Stage) ShownMessage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("game.fxml"));
        Scene scene = new Scene(loader.load());
        try {
            Platform.runLater(() -> {
                stage.setScene(scene);
                GameController controller = loader.getController();
                controller.setUsername(username);
                controller.setOpponentId(opponentId);
                stage.show();
            });

        }
        catch(Exception e){
            System.out.println((e.getMessage()));
            System.out.println((e.getStackTrace()));
        }
    }

    @FXML
    public void LogoutUser() throws IOException {
        String message = "/Quit";
        ClientService.setInstance();
        client.sendText(message);
        try {
            client.sendText("/Quit");
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
