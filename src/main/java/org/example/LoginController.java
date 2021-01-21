package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
    private final int MIN_LENGTH = 3;

    @FXML
    Button loginButton;
    @FXML
    TextField loginTextField;

    @FXML
    public void onLoginButtonClicked(){
        String textValue = loginTextField.getText();
        if(textValue.trim().length() > MIN_LENGTH){
            //TODO: login
        }
    }
}
