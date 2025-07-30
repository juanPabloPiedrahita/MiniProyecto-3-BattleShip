package com.example.miniproyecto3.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Utility class providing visual enhancements and UI feedback mechanisms
 * for the Battleship game. This class supports temporary label displays
 * and informational alert dialogs to improve user experience.

 * This helper is typically used by {@link GameController} to provide
 * visual feedback for game events such as scoring or turn changes.

 * @author David Taborda Montenegro.
 * @version 3.0
 * @since version 3.0
 */
public class UIVisualHelper {

    /**
     * Displays a temporary message in a label with fade-in, pause, and fade-out effects.
     * This is useful for showing brief notifications such as "Ship sunk!" or
     * "Player's turn" without needing to manage the animation manually.
     *
     * @param label The Label node to display the message in.
     * @param text The text to display inside the label.
     */
    public static void showTemporaryLabel(Label label, String text) {
        label.setText(text);
        label.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(_ -> label.setVisible(false));

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.play();
    }

    /**
     * Shows a modal information alert dialog with a given title and message.
     * This is typically used to display game messages such as
     * "You won!" or "You lose!".
     *
     * @param title The title of the alert window.
     * @param message The content message to be displayed in the alert.
     * @see Alert
     */
    public static void showGameAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
