package com.example.miniproyecto3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.miniproyecto3.controller.WelcomeController;
import com.example.miniproyecto3.view.WelcomeStage;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.controller.GameController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        WelcomeStage.getInstance();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
