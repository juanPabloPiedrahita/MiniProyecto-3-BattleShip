package com.example.miniproyecto3.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import com.example.miniproyecto3.model.players.Player;
import com.example.miniproyecto3.exceptions.VisualException;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import javafx.scene.layout.*;
import com.example.miniproyecto3.model.MusicPlayer;
import javafx.scene.text.TextAlignment;

/**
 * Controller class for handling the Welcome Stage of the application.
 * Manages player input, initialization of music, game state continuity, and UI interactions.
 * It also supports file checking and visual feedback before entering the game.

 * @author David Taborda Montenegro and Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 2.0
 */
public class WelcomeController {

    /** Visual reference to handle the view behavior. */
    @FXML
    private TextField userTxt;

    /** Attribute for the manage of the plane text file. */
    private PlaneTextFileHandler planeTextFileHandler;

    /** Boolean flag to know if the player wants to continue a game. */
    public boolean onContinue;

    /** Reference to handle the music. */
    MusicPlayer musicPlayer;

    /** Attribute to remember the player. */
    Player player;

    /**
     * Initializes the welcome screen by playing background music and
     * ensuring the existence of the player data file.
     * If the file does not exist, it creates one with default values.
     */
    @FXML
    public void initialize(){
        musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/MenuMainTheme.mp3");
        musicPlayer.play();
        planeTextFileHandler = new PlaneTextFileHandler();

        try {
            File file = new File("PlayerData.csv");
            if(!file.exists()){
                planeTextFileHandler.write("PlayerData.csv", "default" + "," + 0);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event triggered when the user clicks the "Play" button.
     * Loads player data from the plane text file or creates a new one if the username is new,
     * and initializes the game stage.
     *
     * @throws VisualException if there is an error while loading the visual stage.
     * @throws IOException if an I/O operation fails.
     * @see com.example.miniproyecto3.exceptions.VisualException
     */
    @FXML
    public void onHandlePlayButton() throws VisualException, IOException {
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0];
        int score = Integer.parseInt(data[1]);
        if(!userTxt.getText().isEmpty()) {
            try {
                if(Objects.equals(user, userTxt.getText())) {
                    player = new Player(user, score);
                }
                else {
                    player = new Player(userTxt.getText().trim(), 0);
                    planeTextFileHandler.write("PlayerData.csv", userTxt.getText().trim() + "," + 0);
                }
                GameStage.getInstance();
                onContinue = false;
                musicPlayer.stop();
            } catch (VisualException e) {
                throw new VisualException("Error visual al iniciar el juego: " + e.getMessage());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "¡Ingresa un usuario antes de continuar!");
            DialogPane dialogPane = alert.getDialogPane();
            URL cssUrl = getClass().getResource("/com/example/miniproyecto3/css/styles-game2.css");
            if (cssUrl != null) {
                dialogPane.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("No se encontró el archivo CSS: /com/example/miniproyecto3/css/styles-game2.css");
            }
            dialogPane.getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event triggered when the user clicks the "Continue" button.
     * Loads previous player data and restores the game state if a save file exists.
     * If no file exists, a warning alert is shown to the user.
     */
    @FXML
    public void onHandleContinueButton() {
        File file = new File("GameState.ser");
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0];
        int score = Integer.parseInt(data[1]);
        if(file.exists()) {
            try {
                player = new Player(user,score);
                onContinue = true;
                GameStage.getInstance();
                musicPlayer.stop();
            } catch (VisualException e) {
                System.out.println("Error visual al iniciar el juego: " + e.getMessage());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No existe una partida anterior. Crea una partida nueva.");
            DialogPane dialogPane = alert.getDialogPane();
            URL cssUrl = getClass().getResource("/com/example/miniproyecto3/css/styles-game2.css");
            if (cssUrl != null) {
                dialogPane.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Error con el archivo CSS: /com/example/miniproyecto3/css/styles-game2.css");
            }
            dialogPane.getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event triggered when the user clicks the "Quit" button.
     * Closes the current welcome stage instance.
     */
    @FXML
    public void onHandleQuitButton() {
        WelcomeStage.deleteInstance();
    }

    /**
     * Returns whether the player chose to continue a previous game or not.
     *
     * @return true if continuing a saved game, false otherwise.
     */
    public boolean getContinue() {
        return onContinue;
    }

    /**
     * Returns the player currently interacting with the welcome screen.
     *
     * @return the current {@link Player} instance.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Handles the event triggered when the user clicks the "Credits" button.
     * Displays a styled dialog with information about the game's title,
     * authorship, and a thank-you message for the player.
     */
    @FXML
    public void onHandleCreditsButton() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Créditos del Juego");
        alert.setHeaderText(null);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(550);
        dialogPane.setMinHeight(350);
        URL cssUrl = getClass().getResource("/com/example/miniproyecto3/css/styles-game2.css");
        if (cssUrl != null) {
            dialogPane.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("El archivo CSS /com/example/miniproyecto3/css/styles-game2.css, no se halla.");
        }
        dialogPane.getStyleClass().add("custom-alert");

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        Label title = new Label("BATALLA NAVAL");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label authors = buildAuthorLabel();

        Label thanks = new Label("\n¡Gracias por jugar!");
        thanks.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        content.getChildren().addAll(title, authors, thanks);
        dialogPane.setContent(content);

        ButtonType closeButton = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(closeButton);

        alert.showAndWait();
    }

    /**
     * Builds and returns a styled label with the game authors' information.
     *
     * @return A configured Label node for the credit dialog.
     */
    private Label buildAuthorLabel() {
        String authorText = """
        Desarrollado por:

        Juan Pablo Piedrahita Triana      202342374-3743      juan.pablo.piedrahita@correounivalle.edu.co
        
        David Taborda Montenegro          202242264-3743      taborda.david@correounivalle.edu.co
        """;

        Label authors = new Label(authorText);
        authors.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
        authors.setWrapText(true);
        authors.setTextAlignment(TextAlignment.CENTER);
        authors.setAlignment(Pos.CENTER);

        return authors;
    }
}
