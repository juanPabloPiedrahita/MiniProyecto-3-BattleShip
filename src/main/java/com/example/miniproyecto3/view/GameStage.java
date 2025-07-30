package com.example.miniproyecto3.view;

import com.example.miniproyecto3.exceptions.VisualException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents the main game window (stage) for the Battleship game.
 * This class handles the loading and display of the game UI defined in the FXML file.
 * It uses a Singleton pattern to ensure only one instance is active at a time.

 * @author David Taborda Montenegro and Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 1.0
 * @see Stage
 * @see com.example.miniproyecto3.exceptions.VisualException
 */
public class GameStage extends Stage {

    /**
     * Constructs the game stage, loading the corresponding FXML and setting the stage properties.
     *
     * @throws VisualException if the FXML file or resources cannot be loaded properly.
     */
    public GameStage() throws VisualException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxmls/game-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/miniproyecto3/Image/LogoModerno.jpg")).toExternalForm());
            getIcons().add(icon);
            setResizable(false);
            setTitle("Miniproyecto 3 - Batalla Naval");
            setScene(scene);
            setOnCloseRequest(_ -> {
                deleteInstance();
                Platform.exit();
                System.exit(0);
            });
            show();
        } catch (IOException | NullPointerException e) {
            throw new VisualException("Error al cargar el archivo 'game-view'. " + e.getMessage());
        }
    }

    /**
     * Holder class for the singleton instance of GameStage.
     */
    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

    /**
     * Returns the singleton instance of the GameStage. If it doesn't exist, it will be created.
     *
     * @return the singleton GameStage instance.
     * @throws VisualException if the stage cannot be constructed.
     */
    public static GameStage getInstance() throws VisualException {
        GameStage.GameStageHolder.INSTANCE =
                GameStage.GameStageHolder.INSTANCE != null ?
                        GameStage.GameStageHolder.INSTANCE : new GameStage();
        return GameStage.GameStageHolder.INSTANCE;
    }

    /**
     * Destroys the current instance of GameStage and closes the stage.
     */
    public static void deleteInstance() {
        GameStage.GameStageHolder.INSTANCE.close();
        GameStage.GameStageHolder.INSTANCE = null;
    }
}
