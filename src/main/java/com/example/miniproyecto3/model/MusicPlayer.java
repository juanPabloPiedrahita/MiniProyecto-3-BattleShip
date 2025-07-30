package com.example.miniproyecto3.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Handles audio playback for background music using JavaFX's {@link MediaPlayer}.

 * This class is designed to load and play looping background music in the game.

 * It safely loads a media resource from the classpath and provides methods to play and stop the music.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 * @see MediaPlayer
 * @see Media
 */
public class MusicPlayer {
    /**
     * An instance of the MediaPlayer class, for the playback of the music.
     */
    private MediaPlayer mediaPlayer;

    /**
     * Constructs the MediaPlayer instance with the specified audio file path.
     * The music is configured to loop indefinitely and play at 20% volume.
     *
     * @param resourcePath the path to the audio file (relative to the classpath).
     * @throws IllegalStateException if the audio file cannot be found in the classpath.
     */
    public MusicPlayer(String resourcePath) {
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource == null) {
                throw new IllegalStateException("El audio " + resourcePath + " no se encuentra.");
            }
            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.2);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo de audio: " + e.getMessage());
        }
    }

    /**
     * Starts playback of the audio.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Stops playback of the audio.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
