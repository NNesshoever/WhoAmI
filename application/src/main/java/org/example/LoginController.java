package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import services.ClientService;

import java.io.IOException;

public class LoginController {

    @FXML
    Button loginButton;
    @FXML
    TextField loginTextField;
    @FXML
    Label formValidationLabel;
    private String loginTextFieldValue = "";

    @FXML
    public void onLoginTextFieldKeyTyped() {
        loginTextFieldValue = loginTextField.getText().trim();
    }

    @FXML
    public void onLoginTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onLoginButtonClicked();
        }
    }

    @FXML
    public void onLoginButtonClicked() {
        formValidationLabel.setText("");
        if (loginTextFieldValue.length() >= 3) {
            try {
                ClientService.getInstance(loginTextFieldValue);
                Stage stage = (Stage) loginTextField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(App.class.getResource("Startseite.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            formValidationLabel.setText("Dein Spielername muss mind. aus 3 Zeichen bestehen");
        }
    }
}
