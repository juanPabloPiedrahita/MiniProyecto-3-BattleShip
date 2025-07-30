package com.example.miniproyecto3;

import com.example.miniproyecto3.exceptions.VisualException;
import javafx.application.Application;
import javafx.stage.Stage;
import com.example.miniproyecto3.view.WelcomeStage;

/**
 * Main class that serves as the entry point of the Battleship game application.
 * It initializes and launches the JavaFX application by displaying the WelcomeStage.

 * Any visual-related issues during the loading of the initial screen are handled
 * through a {@link VisualException}.

 * @author David Taborda Montenegro and Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 1.0
 * @see com.example.miniproyecto3.exceptions.VisualException
 * @see Application
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by initializing the {@link WelcomeStage}.
     * If the welcome screen fails to load, a {@link VisualException} is caught and its
     * message is printed to the console.
     *
     * @param primaryStage the primary stage provided by JavaFX (unused, since custom stages are used).
     * @see WelcomeStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            WelcomeStage.getInstance();
        } catch (VisualException ex) {
            System.out.println("Error al cargar el archivo 'home-view'." + ex.getMessage());
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
