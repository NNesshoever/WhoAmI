package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import server.Client;
import services.ClientService;

import java.io.IOException;

public class StartseiteController {

    @FXML
    Button RefreshButton;
    @FXML
    Button LogoutButton;
    @FXML
    TextField usernameTextField;

    private ClientService _clientService;

    public StartseiteController(){
        try {
            String textValue = usernameTextField.getText();
            _clientService = ClientService.getInstance(textValue);
        }
        catch (IOException e){//TODO: Open error Toast
        }
    }

    @FXML
    public void RefreshPlayers() {

    }

    public void SendGameRequest() {
        String message = "/GameRequest " + _clientService.getClientId();
        try {
            _clientService.sendText(message);
        }
        catch (IOException e){
            //TODO: Open error toast or something like that
        }

    }

    public void StartGame() {

    }


}
