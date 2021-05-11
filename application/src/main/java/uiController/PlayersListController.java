package uiController;

import entrypoint.App;
import enums.Commands;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.DataPayload;
import models.User;
import services.ClientService;

import java.io.IOException;
import java.util.ArrayList;

public class PlayersListController {
    @FXML
    ListView<User> playersList;
    private ObservableList<User> usersList;
    private ClientService _clientService;

    @FXML
    public void initialize() throws IOException {
        usersList = FXCollections.observableArrayList();
        playersList.setItems(usersList);
        startThread();
        loadClientList();
    }

    @FXML
    public void loadClientList() throws IOException {
        _clientService.getInstance().sendDataPayload(new DataPayload(Commands.GET_CLIENT_LIST.value));
    }

    public void sendGameRequest(MouseEvent mouseEvent) {
        if (playersList.getSelectionModel().getSelectedItem() != null && mouseEvent.getClickCount() == 2) {
            DataPayload dataPayload = new DataPayload(Commands.SEND_GAME_REQUEST.value, new String[]{
                    Integer.toString(playersList.getSelectionModel().getSelectedItem().getId())});
            try {
                _clientService.getInstance().sendDataPayload(dataPayload);
            } catch (IOException ignored) {
            }
        }
    }

    public void startThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    DataPayload dataPayload = (DataPayload) _clientService.getInstance()
                            .getObjectInputStream().readObject();
                    if (dataPayload.getCommand().equals(Commands.FORWARD_GAME_REQUEST.value)) {
                        displayRequest(dataPayload);
                    } else if (dataPayload.getCommand().equals(Commands.FORWARD_RESPONSE_GAME_REQUEST.value)) {
                        boolean isAccepted = Boolean.parseBoolean(dataPayload.getPlainData().toString());
                        if (isAccepted) {
                            openGameView();
                        }else {
                            System.out.println("abgelehnt");
                        }
                        System.out.println(isAccepted);
                    } else if (dataPayload.getCommand().equals(Commands.ANSWER_CLIENT_LIST.value)) {
                        usersList.setAll((ArrayList<User>) dataPayload.getPlainData());
                    }
                } catch (Exception ignored) {
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
    }

    public void displayRequest(DataPayload dataPayload) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Du hast eine Spielanfrage von ")
                .append(dataPayload.getData()[1])
                .append("(").append(dataPayload.getData()[0]).append(") erhalten");

        Platform.runLater(() -> {
            ButtonType acceptButton = new ButtonType("Akzeptieren", ButtonBar.ButtonData.OK_DONE);
            ButtonType declineButton = new ButtonType("Ablehnen", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert dialog = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    stringBuilder.toString(),
                    acceptButton,
                    declineButton
            );
            dialog.setHeaderText("Spielanfrage");

            Scene currentScene = playersList.getScene();
            Window currentWindow = currentScene.getWindow();
            dialog.setX(currentWindow.getX() + (currentWindow.getWidth() - dialog.getDialogPane().getWidth()) / 2);
            dialog.setY(currentWindow.getY() + (currentWindow.getHeight() - dialog.getDialogPane().getHeight()) / 2);

            dialog.showAndWait()
                    .filter(response -> response.equals(acceptButton) || response.equals(declineButton))
                    .ifPresent(response -> {
                        if (response.equals(acceptButton)) {
                            acceptRequest();
                        } else if (response.equals(declineButton)) {
                            declineRequest();
                        }
                    });
        });

    }

    public void acceptRequest() {
        respondToGameRequest(true);
    }

    public void declineRequest() {
        respondToGameRequest(false);
    }

    private void respondToGameRequest(boolean isAccepted) {
        try {
            DataPayload dataPayload = new DataPayload(Commands.SEND_RESPONSE_GAME_REQUEST.value,
                    Boolean.toString(isAccepted));
            _clientService.getInstance().sendDataPayload(dataPayload);
            openGameView();
        } catch (IOException ignored) {
        }
    }

    public synchronized void openGameView() throws IOException {
        Stage stage = (Stage) playersList.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("game.fxml"));
        try {
            Platform.runLater(() -> {
                Scene scene = null;
                try {
                    scene = new Scene(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(scene);
                stage.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutUser() {
        try {
            DataPayload dataPayload = new DataPayload(Commands.SEND_LOGOUT.value);
            _clientService.getInstance().sendDataPayload(dataPayload);

            Stage stage = (Stage) playersList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
