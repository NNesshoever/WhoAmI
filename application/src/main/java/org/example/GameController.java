package org.example;

import Dtos.PersonDto;
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
import services.ClientService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController {
    private ClientService _clientService;
    private String username = "";
    private final String OPPONENT = "Mitspieler";
    private int opponentId;
    private final String CHAT_USERNAME = "Du";
    private boolean gameIsRunning = true;

    PersonDto person;
    ObservableList<String> listMessages;

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
    @SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
    private final String base64Img = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAEPElEQVR4Xu2Xz2scZRjHn+edFWJPexKtwR5KclAyM8ugEKGwSBGKCl4WaWuoKP4NXipSLV78F1Ro1LYQhN6EqpiDEBR25wf1lKWEYIInySUsws77yGALS8laZinDwPe71+z77n4+++G7GxU+oA0oND3hhQGAR8AAGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAGAGwDH5wIwAHAD4PhcAAYAbgAcnwvAAMANgONzARgAuAFwfC4AAwA3AI7PBWAA4AbA8bkADADcADg+F4ABgBsAx+cCMABwA+D4XAAGAG4AHJ8LwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4PheAAYAbAMfnAjAAcAPg+FwABgBuAByfC8AAwA2A43MBGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAG020CSJKfKsrxqZgMz6zrnRmb2WZ7nvy76znu93hkzu+69f83M1Dn3U6fTuTocDvcXvTOO476qfmxmsZn97Zy7vbS09PnOzs5k0TubONfqBUiS5Cnv/c9mdm5Whve+DILgrTRNf6gr6cGH/7uIPDN71sz+MrOXi6L4s+6dcRy/LSLfi4h75Owv3W739e3t7WndO5t6fqsDiKLofVX9ao6MvSzLzoqIryMrjuNvROTdk86o6tdpmn5Q575+v985OjraE5HnTzpnZht5nn9b584mn9vqAMIw/M45d2meEOfcymg0GtcRFobhgXPu9ElnvPf7RVGcqXNfFEUvqeq9/zmzmWXZlTp3NvncVgcQx/FNEbn4JANYW1s7DILguScVQK/Xe9HM/pj3Hr33N4qieK/JD7XOa7U6gMd8BdzPsmyl7ldAFEWbqroxR9KXWZZ9WEfgYDAIdnd3q6+A5TnnLmdZVoXcykerA6h+BJZleVdE+rP2vPdTVX0jz/Pqb7UeSZK8MJ1Of1PVZx85eKCqr6RpeljrQhGJouhNM7vjnAtmz5rZj6urqxe2trbKunc29fxWB1BJWF9ff3oymXxkZu+ISFdVh2b2aZ7n1S/5hR5hGC4HQXCtLMvz1QXOubuq+skiH/7DNxDH8asiUv272vPeV/8G3jo+Pv5iPB7/s9CbbOhQ6wNoyAPsyzAA2I/+P3AGwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4PheAAYAbAMfnAjAAcAPg+FwABgBuAByfC8AAwA2A43MBGAC4AXB8LgADADcAjs8FYADgBsDxuQAMANwAOD4XgAGAGwDH5wIwAHAD4PhcAAYAbgAcnwvAAMANgONzARgAuAFwfC4AAwA3AI7PBWAA4AbA8bkADADcADg+F4ABgBsAx+cCMABwA+D4XAAGAG4AHJ8LwADADYDjcwEYALgBcHwuAAMANwCOzwVgAOAGwPG5AAwA3AA4/r+aSe6B2O4AawAAAABJRU5ErkJggg==";

    @FXML
    Label labelDetails;

    @FXML
    public void initialize() throws IOException {
        PersonDto person = _clientService.getInstance(this.username).getPerson();
        listMessages = FXCollections.observableArrayList();
        listviewMessages.setItems(listMessages);
        byte[] imageBytes = Base64.getDecoder().decode(base64Img);

        imageView.setImage(new Image((new ByteArrayInputStream(imageBytes))));
        textfieldMessage.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
        setActiveMessage();
        String Details = person.toString();
        labelDetails.setText(Details);
    }

    public void setPerson(PersonDto RandomPerson){
        person = RandomPerson;
    }

    @FXML
    public void onButtonGuessedClicked() throws IOException {
        openModal(false);
        _clientService.getInstance(this.username)
                .sendText("/opponentLost " + opponentId + " " + _clientService.getInstance(this.username).getClientId());
    }

    @FXML
    public void onButtonSurrenderClicked() throws IOException {
        openModal(false);
        _clientService.getInstance(this.username)
                .sendText("/opponentLost " + opponentId + " " + _clientService.getInstance(this.username).getClientId());
    }

    @FXML
    public void openModal(boolean won){
        String modalMessage = "";
        if(won == true){
            modalMessage = "Sie haben gewonnen!";
        }
        else{
            modalMessage = "Sie haben verloren... :(";
        }
        String finalModalMessage = modalMessage;

        Platform.runLater(() -> {
            Alert dialog = new Alert(
                    Alert.AlertType.INFORMATION,
                    finalModalMessage
            );
            dialog.showAndWait()
                    .filter(response -> response.equals(ButtonType.OK))
                    .ifPresent(response -> openStartseite());
        });
    }

    @FXML
    public void openStartseite(){
        try {
            Stage stage = (Stage) buttonGuessed.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("Startseite.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void OnButtonSendClicked() {
        sendMessage();
    }

    private void sendMessage() {
        String text = textfieldMessage.getText().trim();
        if (text.length() > 0) {
            addItem(listviewMessages,CHAT_USERNAME+ ": " + text);
            textfieldMessage.clear();
            sendMessageToServer(text);
        }
    }

    public void sendMessageToServer(String userMessage) {
        try {
            String message = "/SendTextMessage," + userMessage+ ","+ opponentId;
            _clientService.getInstance(this.username).sendText(message);
        } catch (IOException e) {
            //TODO: Open error toast or something like that
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }


    public void setActiveMessage(){
        Thread t = new Thread(() -> {
            while (gameIsRunning) {
                try {
                    String textMessage = _clientService.getInstance(this.username).getLatestTextMessage();
                    if(textMessage.length()>0){
                        if(textMessage.startsWith("/opponentLost")){
                            gameIsRunning = false;
                            openModal(true);
                        }else if(!textMessage.startsWith("/GameRequest")) {
                            Platform.runLater(() -> {
                                addItem(listviewMessages, OPPONENT + ": " + textMessage);
                            });
                        }
                    }
                    } catch (Exception e) {
                }
                try {
                    Thread.sleep(10);

                } catch (InterruptedException ignored) {

                }
            }

        });
        t.start();
    }


    public static <T> void addItem(ListView<T> listView, T item) {
        List<T> messages = listView.getItems();
        int lastIndex = messages.size();
        messages.add(item);
        listView.scrollTo(lastIndex);
    }
}
