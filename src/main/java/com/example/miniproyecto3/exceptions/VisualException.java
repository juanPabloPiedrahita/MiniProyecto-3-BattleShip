package com.example.miniproyecto3.exceptions;

/**
 * Exception thrown when a visual-related error occurs in the application.

 * This exception is used to signal problems such as missing or corrupted
 * visual resources or issues encountered while rendering the user interface.

 * It allows the application to differentiate between logic errors and visual
 * presentation problems, facilitating targeted error handling.

 * @author David Taborda Montenegro.
 * @version 3.0
 * @since version 3.0
 * @see Exception
 */
public class VisualException extends Exception {

    /**
     * Constructs a new VisualException with the specified detail message.
     *
     * @param message the detail message explaining the nature of the visual error.
     */
    public VisualException(String message) {
        super(message);
    }
}
