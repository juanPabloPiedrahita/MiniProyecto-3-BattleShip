package com.example.miniproyecto3;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.miniproyecto3.view.WelcomeStage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        WelcomeStage.getInstance();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
