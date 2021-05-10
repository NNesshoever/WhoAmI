package uiController;

import enums.Commands;
import models.User;
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
import services.ClientService;
import entrypoint.App;

import java.io.IOException;
import java.util.ArrayList;

public class StartseiteController {
    ObservableList<User> usersList;

    ClientService client;

    @FXML
    ListView<User> PlayersList;

    private ClientService _clientService;
    private String username = "";
    private int opponentId;
    private String message;
    private String messageToShow;


    @FXML
    public void initialize() {
        usersList = FXCollections.observableArrayList();
        PlayersList.setItems(usersList);
        loadClientList();
        message = null;
        startThread();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void loadClientList() {
        try {
            client = ClientService.getInstance(this.username);
            ArrayList<User> users = client.getClientList();
            users.removeIf((clientDto) -> clientDto.getId() == client.getClientId());
            usersList.setAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void RefreshPlayers() {
        loadClientList();
    }

    public void SendGameRequest(MouseEvent mouseEvent) throws IOException {
        if (PlayersList.getSelectionModel().getSelectedItem() != null) {
            String message = Commands.SEND_GAME_REQUEST.value + " " + PlayersList.getSelectionModel().getSelectedItem().getId() + " " + _clientService.getInstance(this.username).getClientId();
            opponentId = PlayersList.getSelectionModel().getSelectedItem().getId();
            try {
                _clientService.getInstance(this.username).sendText(message);
            } catch (IOException e) {
                //TODO: Open error toast or something like that
            }
        }
    }

    public void startThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    String commandMessage = ClientService.getInstance(this.username).getObjectInputStream().readUTF();
                    if (commandMessage.startsWith(Commands.SEND_GAME_REQUEST.value)) {
                        opponentId = Integer.parseInt(commandMessage.split(" ")[1]);
                        displayRequest();
                    } else if (commandMessage.startsWith(Commands.REPLY_SEND_GAME_REQUEST.value)) {
                        opponentId = Integer.parseInt(commandMessage.split(" ")[1]);
                        OpenGameView();
                    }

                } catch (Exception e) {
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {

                }
            }
        });
        t.start();
    }

    public void displayRequest() {
        String message = prepareMessage();
        Platform.runLater(() -> {
            ButtonType acceptButton = new ButtonType("Akzeptieren", ButtonBar.ButtonData.OK_DONE);
            ButtonType declineButton = new ButtonType("Ablehnen", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert dialog = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    message,
                    acceptButton,
                    declineButton
            );
            dialog.setHeaderText("Spielanfrage");

            dialog.showAndWait()
                    .filter(response -> response.equals(acceptButton) || response.equals(declineButton))
                    .ifPresent(response -> {
                        if (response.equals(acceptButton)) {
                            AcceptRequest();
                        } else if (response.equals(declineButton)) {
                            DeclineRequest();
                        }
                    });
        });

    }

    public String prepareMessage() {
        String actualMessage = message.substring(0, message.lastIndexOf(" "));
        String playerId = message.substring(message.lastIndexOf(" "));

        switch (actualMessage) {
            case "/GameRequest":
                messageToShow = "You got a game request from " + playerId + ". Do you accept?";
                break;
            default:
                message = null;
        }

        return messageToShow;
    }

    public void AcceptRequest() {
        String otherPlayerId = splitMessage();
        try {
            String message = Commands.ACCEPT_GAME_REQUEST.value + " " + otherPlayerId + " " + _clientService.getInstance(username).getClientId();
            _clientService.getInstance(this.username).sendText(message);
            OpenGameView();
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public void DeclineRequest() {

        String otherPlayerId = splitMessage();
        try {
            String message = "/Decline " + otherPlayerId + " " + _clientService.getInstance(username).getClientId();
            _clientService.getInstance(this.username).sendText(message);
            _clientService.getInstance(this.username).setPayload(null);
            this.message = null;

        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public String splitMessage() {
        String[] messageParts = messageToShow.split("\\s+");
        return messageParts[6].substring(0, messageParts[6].length() - 1);
    }

    public synchronized void OpenGameView() throws IOException {
        Stage stage = (Stage) PlayersList.getScene().getWindow();
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
                GameController controller = loader.getController();
                controller.setUsername(username);
                controller.setOpponentId(opponentId);
                stage.show();
            });
        } catch (Exception e) {
            System.out.println((e.getMessage()));
            System.out.println((e.getStackTrace()));
        }

    }

    @FXML
    public void LogoutUser() throws IOException {
        String message = Commands.QUIT.value;
        ClientService.setInstance();
        client.sendText(message);
        try {
            client.sendText("/Quit");
            Stage stage = (Stage) PlayersList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            LoginController controller = loader.getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
