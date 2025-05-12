package com.example.miniproyecto3.controller;

//import java.io.IO;
import java.io.IOException;
import com.example.miniproyecto3.view.WelcomeStage;

public class WelcomeController {

    public void initialize(){


    }


    public void onHandlePlayButtom(javafx.event.ActionEvent event) throws IOException {
        WelcomeStage.deleteInstance();
    }
}
