package com.example.miniproyecto3.controller;

//import java.io.IO;
import java.io.IOException;

import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import com.example.miniproyecto3.model.serializable.SerializableFileHandler;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;



public class WelcomeController {
    //atributos
    @FXML private TextField userTxt;
    public String username;
    public SerializableFileHandler serializableFileHandler;
    public PlaneTextFileHandler planeTextFileHandler;


    public void initialize(){
        //serializableFileHandler = new SerializableFileHandler();
        planeTextFileHandler = new PlaneTextFileHandler();
    }

    public void onHandlePlayButtom(javafx.event.ActionEvent event) throws IOException {
        WelcomeStage.deleteInstance();

        username = userTxt.getText().trim();
        planeTextFileHandler.write("playerData.csv", username);


        GameStage.getInstance();

    }

    public void onHandleQuitButtom(javafx.event.ActionEvent event) throws IOException {
        WelcomeStage.deleteInstance();
    }
}
