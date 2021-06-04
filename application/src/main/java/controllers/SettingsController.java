package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import services.ClientService;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SettingsController {


    @FXML
    TextField textFieldHost;
    @FXML
    TextField textFieldPort;

    @FXML
    private void initialize() {
        try {
            textFieldHost.setText(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        textFieldPort.setText(String.valueOf(ClientService.getInstance().getPort()));
        textFieldPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldPort.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    @FXML
    private void onSaveSettings() {
        String textHost = textFieldHost.getText().trim();
        String textPort = textFieldPort.getText().trim();
        if (!textHost.isEmpty() && !textPort.isEmpty()) {
            ClientService.getInstance().setHost(textHost);
            ClientService.getInstance().setPort(Integer.parseInt(textPort));

            Platform.runLater(() -> {
                Alert dialog = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Deine Einstellungen wurden gespeichert"
                );
                dialog.setHeaderText("Gespeichert");
                centerDialog(dialog);
                dialog.showAndWait();
            });
        }
    }

    private void centerDialog(Alert dialog) {
        Scene currentScene = textFieldHost.getScene();
        Window currentWindow = currentScene.getWindow();
        dialog.setX(currentWindow.getX() + (currentWindow.getWidth() - dialog.getDialogPane().getWidth()) / 2);
        dialog.setY(currentWindow.getY() + (currentWindow.getHeight() - dialog.getDialogPane().getHeight()) / 2);
    }
}
