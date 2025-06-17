package com.example.miniproyecto3.controller;

//import java.io.IO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.example.miniproyecto3.model.Players.Player;
import com.example.miniproyecto3.exceptions.VisualException;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import javafx.scene.layout.*;
import com.example.miniproyecto3.model.MusicPlayer;


public class WelcomeController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField userTxt;

    private PlaneTextFileHandler planeTextFileHandler;

    public boolean onContinue;

    MusicPlayer musicPlayer;

    Player player;

    @FXML
    public void initialize(){
        musicPlayer = new MusicPlayer("/com/example/miniproyecto3/media/MenuMainTheme.mp3");
        musicPlayer.play();
        /*BackgroundImage fondo = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100, 100, true, true, true, false
                )
        );
        borderPane.setBackground(new Background(fondo));*/
        planeTextFileHandler = new PlaneTextFileHandler();

        //player = new Player(userTxt.getText(),0);
    }

    @FXML
    public void onHandlePlayButtom(javafx.event.ActionEvent event) throws VisualException {
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0];
        int score = Integer.parseInt(data[1]);
        if(!userTxt.getText().isEmpty()) {
            try {
                if(Objects.equals(user, userTxt.getText())) {
                    player = new Player(user, score);
                    GameStage.getInstance();
                    onContinue = false;
                    musicPlayer.stop();
                }
                else {
                    player = new Player(userTxt.getText().trim(), 0);
                    planeTextFileHandler.write("PlayerData.csv", userTxt.getText() + "," + 0);
                    //WelcomeStage.deleteInstance();
                    GameStage.getInstance();
                    onContinue = false;
                    musicPlayer.stop();
                }
            } catch (VisualException e) {
                throw new VisualException("Error visual al iniciar el juego: " + e.getMessage());
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ingresa un usuario antes de continuar!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/com/example/miniproyecto3/CSS/game-style2.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }

    @FXML
    public void onHandleContinueButtom(javafx.event.ActionEvent event) {
        File file = new File("GameState.ser");
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0];
        int score = Integer.parseInt(data[1]);
        if(file.exists()) {
            try {
                //WelcomeStage.deleteInstance();
                player = new Player(user,score);
                onContinue = true;
                GameStage.getInstance();
                musicPlayer.stop();
            } catch (VisualException e) {
                System.out.println("Error visual al iniciar el juego: " + e.getMessage());
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "No existe una partida anterior, cree una partida nueva!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/com/example/miniproyecto3/CSS/game-style2.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }


    @FXML
    public void onHandleQuitButtom(javafx.event.ActionEvent event) throws IOException {

        WelcomeStage.deleteInstance();
    }

    public boolean getContinue() {
        return onContinue;
    }

    public Player getPlayer(){
        return player;
    }

    public void onHandleCreditsButtom(javafx.event.ActionEvent event) throws IOException {


    }
}
