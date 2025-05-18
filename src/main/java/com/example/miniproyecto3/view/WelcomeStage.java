package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.WelcomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeStage extends Stage {
    WelcomeController welController;

    public WelcomeStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/Fxml's/home-view1.fxml"));
        Parent root = fxmlLoader.load(); //crea una instancia de gameController y llama a initialize()
        welController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        setResizable(false);
        setTitle("BattleShip");
        setScene(scene);
        show();
    }

    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }


    public static WelcomeStage getInstance() throws IOException {
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
