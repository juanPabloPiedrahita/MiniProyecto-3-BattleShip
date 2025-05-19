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
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxml's/game-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Batalla Naval");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
        */
        WelcomeStage.getInstance();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
