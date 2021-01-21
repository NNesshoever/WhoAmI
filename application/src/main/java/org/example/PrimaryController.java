package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
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
            client = ClientService.getInstance();
            client.sendText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
