package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.WelcomeController;
import com.example.miniproyecto3.model.exceptions.VisualException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WelcomeStage extends Stage {
    WelcomeController welController;

    public WelcomeStage() throws VisualException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxml's/home-view1.fxml"));
            Parent root = fxmlLoader.load(); //crea una instancia de gameController y llama a initialize()
            welController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/miniproyecto3/css/styles.css").toExternalForm());
            Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/miniproyecto3/Image/LogoModerno-removebg-preview.png")).toExternalForm());
            getIcons().add(icon);
            setResizable(false);
            setTitle("BattleShip");
            setScene(scene);
            show();
        } catch (IOException e) {
            throw new VisualException("Error al cargar el archivo 'home-view'.");
        } catch (NullPointerException ex) {
            throw new VisualException("Error al cargar el archivo 'home-view'.");
        }

    }

    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }


    public static WelcomeStage getInstance() throws VisualException {
        WelcomeStage.WelcomeStageHolder.INSTANCE =
                WelcomeStage.WelcomeStageHolder.INSTANCE != null ?
                        WelcomeStage.WelcomeStageHolder.INSTANCE : new WelcomeStage();
        return WelcomeStage.WelcomeStageHolder.INSTANCE;
    }

    public static void deleteInstance() {
        WelcomeStage.WelcomeStageHolder.INSTANCE.close();
        WelcomeStage.WelcomeStageHolder.INSTANCE = null;
    }

    public WelcomeController getWelController() {
        return welController;
    }
}
