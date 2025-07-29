package com.example.miniproyecto3.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public MusicPlayer(String resourcePath) {
        try {
            Media media = new Media(getClass().getResource(resourcePath).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.2);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo de audio: " + e.getMessage());
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
