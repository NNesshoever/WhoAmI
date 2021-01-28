package org.example;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import org.example.Dtos.UserDto;
import services.ClientService;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    public void setServerConnection() {
        ClientService client = null;
        try {
            //TODO: Eingabe von Benutzer statt "testUser"
            client = ClientService.getInstance("testUser");
            //client.sendText("");
            ArrayList<UserDto> dtos =  client.getClientList();
            dtos.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
