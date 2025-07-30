package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.WelcomeController;
import com.example.miniproyecto3.exceptions.VisualException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * The WelcomeStage class represents the initial stage (window) of the application,
 * displaying the welcome screen for the user to start or continue a game.

 * It uses the Singleton design pattern to ensure that only one instance of the stage exists at any given time.
 * This class is responsible for loading the FXML view, applying stylesheets,
 * initializing the controller, and configuring visual settings such as icons and titles.

 * If any visual resource fails to load, a {@link VisualException} is thrown.

 * @author David Taborda Montenegro and Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 1.0
 * @see com.example.miniproyecto3.exceptions.VisualException
 * @see Stage
 */
public class WelcomeStage extends Stage {

    /** Attribute for know the controller of this class. */
    WelcomeController welController;

    /**
     * Constructs the welcome stage by loading its FXML layout and configuring the stage.
     *
     * @throws VisualException if the FXML file or resources fail to load properly.
     */
    public WelcomeStage() throws VisualException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxmls/home-view.fxml"));

            Parent root = fxmlLoader.load();
            welController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/miniproyecto3/css/styles.css"), "CSS file not found").toExternalForm());
            Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/miniproyecto3/Image/favicon.jpg")).toExternalForm());
            getIcons().add(icon);
            setResizable(false);
            setTitle("Miniproyecto 3 - Batalla Naval");
            setScene(scene);
            show();
        } catch (IOException | NullPointerException e) {
            throw new VisualException("Error al cargar el archivo 'home-view'." + e.getMessage());
        }
    }

    /**
     * Holder class for implementing the Singleton pattern in a thread-safe manner.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }

    /**
     * Returns the singleton instance of WelcomeStage.
     * If it doesn't exist, a new instance is created.
     *
     * @return the single instance of WelcomeStage.
     * @throws VisualException if the stage fails to initialize.
     */
    public static WelcomeStage getInstance() throws VisualException {
        WelcomeStage.WelcomeStageHolder.INSTANCE =
                WelcomeStage.WelcomeStageHolder.INSTANCE != null ?
                        WelcomeStage.WelcomeStageHolder.INSTANCE : new WelcomeStage();
        return WelcomeStage.WelcomeStageHolder.INSTANCE;
    }

    /**
     * Deletes the current instance of WelcomeStage, closing the window and releasing the reference.
     */
    public static void deleteInstance() {
        WelcomeStage.WelcomeStageHolder.INSTANCE.close();
        WelcomeStage.WelcomeStageHolder.INSTANCE = null;
    }

    /**
     * Returns the controller associated with the welcome stage.
     *
     * @return the {@link WelcomeController} for this stage.
     */
    public WelcomeController getWelController() {
        return welController;
    }
}
