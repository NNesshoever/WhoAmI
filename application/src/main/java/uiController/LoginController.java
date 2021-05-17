package uiController;

import entrypoint.App;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
                FXMLLoader loader = new FXMLLoader(App.class.getResource("playersList.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();
                //TODO handle windows close on click
                // https://stackoverflow.com/questions/26619566/javafx-stage-close-handler
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Stage is closing");
                        Platform.exit();
                        System.exit(0);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            formValidationLabel.setText("Dein Spielername muss mind. aus 3 Zeichen bestehen");
        }
    }
}
