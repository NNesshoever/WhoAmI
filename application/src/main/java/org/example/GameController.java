package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

public class GameController {

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
    public void initialize() {
        listMessages = FXCollections.observableArrayList();
        listviewMessages.setItems(listMessages);
        byte[] imageBytes = Base64.getDecoder().decode(base64Img);

        imageView.setImage(new Image((new ByteArrayInputStream(imageBytes))));
        textfieldMessage.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
    }

    @FXML
    public void onButtonGuessedClicked() {

    }

    @FXML
    public void onButtonSurrenderClicked(){

    }

    @FXML
    public void OnButtonSendClicked() {
        sendMessage();
    }

    private void sendMessage() {
        String text = textfieldMessage.getText().trim();
        if (text.length() > 0) {
            addItem(listviewMessages, text);
            textfieldMessage.clear();
        }
    }

    public static <T> void addItem(ListView<T> listView, T item) {
        List<T> messages = listView.getItems();
        int lastIndex = messages.size();
        messages.add(item);
        listView.scrollTo(lastIndex);
    }
}
