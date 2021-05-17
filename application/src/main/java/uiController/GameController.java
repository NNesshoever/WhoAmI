package uiController;

import entrypoint.App;
import enums.Commands;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.DataPayload;
import models.Person;
import services.ClientService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController {
    private final String OPPONENT = "Mitspieler";
    private final String CHAT_USERNAME = "Du";
    @SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
    private final String base64Img = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAEPElEQVR4Xu2Xz2scZRjHn+edFWJPexKtwR5KclAyM8ugEKGwSBGKCl4WaWuoKP4NXipSLV78F1Ro1LYQhN6EqpiDEBR25wf1lKWEYIInySUsws77yGALS8laZinDwPe71+z77n4+++G7GxU+oA0oND3hhQGAR8AAGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAGAGwDH5wIwAHAD4PhcAAYAbgAcnwvAAMANgONzARgAuAFwfC4AAwA3AI7PBWAA4AbA8bkADADcADg+F4ABgBsAx+cCMABwA+D4XAAGAG4AHJ8LwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4PheAAYAbAMfnAjAAcAPg+FwABgBuAByfC8AAwA2A43MBGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAG020CSJKfKsrxqZgMz6zrnRmb2WZ7nvy76znu93hkzu+69f83M1Dn3U6fTuTocDvcXvTOO476qfmxmsZn97Zy7vbS09PnOzs5k0TubONfqBUiS5Cnv/c9mdm5Whve+DILgrTRNf6gr6cGH/7uIPDN71sz+MrOXi6L4s+6dcRy/LSLfi4h75Owv3W739e3t7WndO5t6fqsDiKLofVX9ao6MvSzLzoqIryMrjuNvROTdk86o6tdpmn5Q575+v985OjraE5HnTzpnZht5nn9b584mn9vqAMIw/M45d2meEOfcymg0GtcRFobhgXPu9ElnvPf7RVGcqXNfFEUvqeq9/zmzmWXZlTp3NvncVgcQx/FNEbn4JANYW1s7DILguScVQK/Xe9HM/pj3Hr33N4qieK/JD7XOa7U6gMd8BdzPsmyl7ldAFEWbqroxR9KXWZZ9WEfgYDAIdnd3q6+A5TnnLmdZVoXcykerA6h+BJZleVdE+rP2vPdTVX0jz/Pqb7UeSZK8MJ1Of1PVZx85eKCqr6RpeljrQhGJouhNM7vjnAtmz5rZj6urqxe2trbKunc29fxWB1BJWF9ff3oymXxkZu+ISFdVh2b2aZ7n1S/5hR5hGC4HQXCtLMvz1QXOubuq+skiH/7DNxDH8asiUv272vPeV/8G3jo+Pv5iPB7/s9CbbOhQ6wNoyAPsyzAA2I/+P3AGwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4PheAAYAbAMfnAjAAcAPg+FwABgBuAByfC8AAwA2A43MBGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAGAGwDH5wIwAHAD4PhcAAYAbgAcnwvAAMANgONzARgAuAFwfC4AAwA3AI7PBWAA4AbA8bkADADcADg+F4ABgBsAx+cCMABwA+D4XAAGAG4AHJ8LwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4/r+aSe6B2O4AawAAAABJRU5ErkJggg==";
    //TODO: Test if fxml vars can be private
    @FXML
    ListView<String> listviewMessages;
    @FXML
    TextField textfieldMessage;
    @FXML
    Button buttonGuessed;
    @FXML
    Button buttonSurrender;
    @FXML
    ImageView imageView;
    @FXML
    Label labelDetails;

    private ObservableList<String> listMessages;
    private AtomicBoolean running = new AtomicBoolean(true);
    private ClientService _clientService;

    public static <T> void addItem(ListView<T> listView, T item) {
        List<T> messages = listView.getItems();
        int lastIndex = messages.size();
        messages.add(item);
        listView.scrollTo(lastIndex);
    }

    @FXML
    public void initialize() throws IOException {
        listMessages = FXCollections.observableArrayList();
        listviewMessages.setItems(listMessages);
        textfieldMessage.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                sendChatMessage();
            }
        });
        startThread();
        loadPerson();
    }

    public void loadPerson() throws IOException {
        DataPayload dataPayload = new DataPayload(Commands.GET_PERSON.value);
        _clientService.getInstance().sendDataPayload(dataPayload);
    }

    public void setImage(String pBase64Img) {
        byte[] imageBytes = Base64.getDecoder().decode(pBase64Img);
        imageView.setImage(new Image((new ByteArrayInputStream(imageBytes))));
    }

    public void retrieveRandomPerson(DataPayload dataPayload) {
        Platform.runLater(() -> {
            Person person = (Person) dataPayload.getPlainData();
            String details = person.toString();
            labelDetails.setText(details);

            if (person.getBase64Image() != null && !person.getBase64Image().isEmpty()) {
                setImage(person.getBase64Image());
            } else {
                setImage(base64Img);
            }
        });
    }

    @FXML
    private void sendOpponentLostPayload() throws IOException {
        openModal(false);
        DataPayload dataPayload = new DataPayload(Commands.SEND_OPPONENT_LOST.value);
        _clientService.getInstance().sendDataPayload(dataPayload);
    }

    @FXML
    public void openModal(boolean won) {
        final String modalMessage;
        if (won) {
            modalMessage = "Du hast gewonnen!";
        } else {
            modalMessage = "Du hast leider verloren... :(";
        }

        Platform.runLater(() -> {
            Alert dialog = new Alert(
                    Alert.AlertType.INFORMATION,
                    modalMessage
            );

            Scene currentScene = listviewMessages.getScene();
            Window currentWindow = currentScene.getWindow();
            dialog.setX(currentWindow.getX() + (currentWindow.getWidth() - dialog.getDialogPane().getWidth()) / 2);
            dialog.setY(currentWindow.getY() + (currentWindow.getHeight() - dialog.getDialogPane().getHeight()) / 2);

            dialog.showAndWait()
                    .filter(response -> response.equals(ButtonType.OK))
                    .ifPresent(response -> openStartseite());
        });
    }

    @FXML
    public void openStartseite() {
        try {
            Stage stage = (Stage) buttonGuessed.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("playersList.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void OnButtonSendClicked() {
        sendChatMessage();
    }

    private void sendChatMessage() {
        String text = textfieldMessage.getText().trim();
        if (text.length() > 0) {
            addItem(listviewMessages, CHAT_USERNAME + ": " + text);
            textfieldMessage.clear();
            sendMessageToServer(text);
        }
    }

    public void sendMessageToServer(String userMessage) {
        try {
            DataPayload dataPayload = new DataPayload(Commands.SEND_TEXT_MESSAGE.value, new String[]{userMessage});
            _clientService.getInstance().sendDataPayload(dataPayload);
        } catch (IOException ignored) {
        }
    }


    public void startThread() {
        Thread t = new Thread(() -> {
            while (running.get()) {
                try {
                    ObjectInputStream inputStream = _clientService.getInstance().getObjectInputStream();
                    Object test = inputStream.readObject();
                    System.out.println("incoming GameController " + test.toString());
                    DataPayload dataPayload = (DataPayload) test;
                    if (dataPayload != null) {
                        if (dataPayload.getCommand().equals(Commands.SEND_OPPONENT_LOST.value)) {
                            running.set(false);
                            openModal(true);
                        } else if (dataPayload.getCommand().equals(Commands.SEND_TEXT_MESSAGE.value)) {
                            Platform.runLater(() -> {
                                addItem(listviewMessages, OPPONENT + ": " + dataPayload.getData()[0]);
                            });
                        } else if (dataPayload.getCommand().equals(Commands.ANSWER_PERSON.value)) {
                            retrieveRandomPerson(dataPayload);
                        } else {
                            System.out.println("Unknown command: " + dataPayload.getCommand());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);

                } catch (InterruptedException ignored) {

                }
            }

        });
        t.start();
    }
}
