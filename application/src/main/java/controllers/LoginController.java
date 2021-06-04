package controllers;

import entrypoint.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private boolean isSettingsWindowOpen = false;

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
    public void onOpenSettingsClicked() {
        if (!isSettingsWindowOpen) {
            Parent root;
            try {
                root = FXMLLoader.load(App.class.getResource("settings.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Einstellungen");
                stage.setScene(new Scene(root, 500, 500));
                stage.getIcons().add(new Image(App.class.getResourceAsStream("images/logo.png")));
                stage.setOnCloseRequest(event -> isSettingsWindowOpen = false);
                isSettingsWindowOpen = true;
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                stage.setOnCloseRequest(we -> {
                    ClientService.getInstance().logout();
                    Platform.exit();
                    System.exit(0);
                });
                stage.show();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (e.getMessage().trim().equalsIgnoreCase("Connection refused: connect")) {
                    Platform.runLater(() -> {
                        Alert dialog = new Alert(
                                Alert.AlertType.ERROR,
                                "Es konnte keine Verbindung zum Server hergestellt werden. " +
                                        "Überprüfe deine Internetverbindung und die Server-URL in den Einstellungen"
                        );
                        dialog.setHeaderText("Verbindungsfehler");

                        Scene currentScene = loginButton.getScene();
                        Window currentWindow = currentScene.getWindow();
                        dialog.setX(currentWindow.getX() + (currentWindow.getWidth() - dialog.getDialogPane().getWidth()) / 2);
                        dialog.setY(currentWindow.getY() + (currentWindow.getHeight() - dialog.getDialogPane().getHeight()) / 2);

                        dialog.showAndWait();
                    });
                }
            }
        } else {
            formValidationLabel.setText("Dein Spielername muss mind. aus 3 Zeichen bestehen");
        }
    }

    public void setSettingsWindowOpen(boolean settingsWindowOpen) {
        isSettingsWindowOpen = settingsWindowOpen;
    }
}
