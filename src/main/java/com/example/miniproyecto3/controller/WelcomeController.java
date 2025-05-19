package com.example.miniproyecto3.controller;

//import java.io.IO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import com.example.miniproyecto3.model.Player;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


public class WelcomeController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField userTxt;

    private PlaneTextFileHandler planeTextFileHandler;

    public boolean onContinue;

    @FXML
    public void initialize(){
        Image backgroundImage = new Image("C:\\Users\\Juanpa\\IdeaProjects\\miniproyecto-3\\src\\main\\resources\\com\\example\\miniproyecto3\\Image\\fondo2.jpg");
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
    public void onHandlePlayButtom(javafx.event.ActionEvent event) throws IOException {
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0];
        if(!userTxt.getText().isEmpty()) {
            if(Objects.equals(user, userTxt.getText())) {
                GameStage.getInstance().getController().continueB(false,true);
                GameStage.getInstance();
                onContinue = false;
            }
            else {
                planeTextFileHandler.write("PlayerData.csv", userTxt.getText() + "," + 0);
                //WelcomeStage.deleteInstance();
                GameStage.getInstance().getController().continueB(false);
                GameStage.getInstance();
                onContinue = false;
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ingresa un usuario antes de continuar!");
            alert.showAndWait();
        }
    }

    @FXML
    public void onHandleContinueButtom(javafx.event.ActionEvent event) throws IOException {
        File file = new File("GameState.ser");
        if(file.exists()) {
            //WelcomeStage.deleteInstance();
            onContinue = true;
            GameStage.getInstance().getController().continueB(true);
            GameStage.getInstance();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "No existe una partida anterior, cree una partida nueva!");
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
}
