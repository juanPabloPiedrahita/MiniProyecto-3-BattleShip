package com.example.miniproyecto3.exceptions;  // Pasarlas a paquete externo.

public class VisualException extends Exception {
    public VisualException(String message) {
        super(message);
    }  //Aquí se plantea la excepción a modo de "interface", pero se 'define' dentro de WelcomeStage y GameStage.
}
