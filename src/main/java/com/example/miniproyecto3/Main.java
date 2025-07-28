package com.example.miniproyecto3;

import com.example.miniproyecto3.exceptions.VisualException;
import javafx.application.Application;
import javafx.stage.Stage;
import com.example.miniproyecto3.view.WelcomeStage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            WelcomeStage.getInstance();
        } catch (VisualException ex) {
            System.out.println("Error al cargar el archivo 'home-view'." + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
