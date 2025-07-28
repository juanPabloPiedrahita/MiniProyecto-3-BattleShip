package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.exceptions.VisualException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameStage extends Stage {

    GameController gameController;

    public GameStage() throws VisualException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxml's/game-view.fxml"));
            Parent root = loader.load();
            gameController = loader.getController();
            Scene scene = new Scene(root);
            setTitle("Batalla Naval");
            setScene(scene);
            setOnCloseRequest(_ -> {
                deleteInstance();
                Platform.exit();
                System.exit(0);
            });
            show();
        } catch (IOException e) {  // Aquí también se define la excepción visual.
            throw new VisualException("Error al cargar el archivo 'game-view'. " + e.getMessage());
        } catch (NullPointerException ex) {  // Aquí se complementa la definición.
            throw new VisualException("Error al cargar el archivo 'game-view'. " + ex.getMessage());
        }

    }

    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

    public static GameStage getInstance() throws VisualException {
        GameStage.GameStageHolder.INSTANCE =
                GameStage.GameStageHolder.INSTANCE != null ?
                        GameStage.GameStageHolder.INSTANCE : new GameStage();
        return GameStage.GameStageHolder.INSTANCE;
    }

    public static void deleteInstance() {
        GameStage.GameStageHolder.INSTANCE.close();
        GameStage.GameStageHolder.INSTANCE = null;
    }

    public GameController getController() {
        return this.gameController;
    }

}
