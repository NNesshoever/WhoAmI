package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void login() throws IOException {
        App.setRoot("menu");
    }
}
