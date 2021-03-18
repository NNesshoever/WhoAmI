package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ClientService;

import java.io.IOException;

public class LoginController {

    @FXML
    Button loginButton;
    @FXML
    TextField loginTextField;

    @FXML
    public void onLoginButtonClicked(){
        String textValue = loginTextField.getText().trim();
        if(textValue.length() >= 3){
            try {
                ClientService.getInstance(textValue);
                Stage stage = (Stage) loginTextField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(App.class.getResource("Startseite.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle(textValue);

                StartseiteController controller = loader.getController();
                controller.setUsername(textValue);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
