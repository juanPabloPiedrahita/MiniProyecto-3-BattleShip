package com.example.miniproyecto3.controller;

//import java.io.IO;
import java.io.File;
import java.util.Objects;

import com.example.miniproyecto3.model.Players.Player;
import com.example.miniproyecto3.exceptions.VisualException;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import javafx.scene.layout.*;
import com.example.miniproyecto3.model.MusicPlayer;
import javafx.event.ActionEvent;
import javafx.scene.text.TextAlignment;


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
    public void onHandlePlayButtom(ActionEvent event) throws VisualException {
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
    public void onHandleContinueButtom(ActionEvent event) {
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
    public void onHandleQuitButtom(ActionEvent event) {

        WelcomeStage.deleteInstance();
    }

    public boolean getContinue() {
        return onContinue;
    }

    public Player getPlayer(){
        return player;
    }

    @FXML
    public void onHandleCreditsButtom() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Créditos del juego");
        alert.setHeaderText(null);

        // Personalizar tamaño y fondo
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(550);
        dialogPane.setMinHeight(350);
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/miniproyecto3/CSS/game-style2.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        // Crear layout con estilo
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        Label title = new Label("BATALLA NAVAL");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label authors = new Label(
                "Desarrollado por:\n\n" +
                        "Juan Pablo Piedrahita Triana      202342...-3743      juan.pablo.piedrahita@correounivalle.edu.co\n" +
                        "David Taborda Montenegro          202242264-3743      taborda.david@correounivalle.edu.co"
        );
        authors.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
        authors.setWrapText(true);
        authors.setTextAlignment(TextAlignment.CENTER);
        authors.setAlignment(Pos.CENTER);

        Label thanks = new Label("\n¡Gracias por jugar!");
        thanks.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        content.getChildren().addAll(title, authors, thanks);
        dialogPane.setContent(content);

        // Botón de cerrar
        ButtonType closeButton = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(closeButton);

        alert.showAndWait();

    }
}
