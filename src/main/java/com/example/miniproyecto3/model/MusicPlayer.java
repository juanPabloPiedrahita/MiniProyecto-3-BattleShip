package com.example.miniproyecto3.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    // Constructor: recibe la ruta del archivo
    public MusicPlayer(String resourcePath) {
        try {
            Media media = new Media(getClass().getResource(resourcePath).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repetir infinitamente
            mediaPlayer.setVolume(0.5); // Volumen inicial (0.0 - 1.0)
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo de audio: " + e.getMessage());
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume); // 0.0 (silencio) a 1.0 (máximo)
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
